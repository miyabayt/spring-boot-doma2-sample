package com.sample.batch.jobs;

import com.sample.common.util.FileUtils;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.batch.core.*;
import org.springframework.batch.core.converter.JobParametersConverter;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobParametersNotFoundException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.batch.JobExecutionEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.util.StringUtils;

/** 単一のジョブのみを処理するCommandLineJobRunner */
@Slf4j
public class SingleJobCommandLineRunner
    implements CommandLineRunner, ApplicationEventPublisherAware {

  @Value("${application.processFileLocation:#{systemProperties['java.io.tmpdir']}}")
  private String processFileLocation;

  public static final String JOB_PARAMETER_JOB_NAME = "--job";

  @Getter @Autowired JobParametersConverter converter;

  @Getter @Autowired JobLauncher jobLauncher;

  @Getter @Autowired JobExplorer jobExplorer;

  @Getter @Autowired Collection<Job> jobs = Collections.emptySet();

  @Getter ApplicationEventPublisher publisher;

  @Override
  public void setApplicationEventPublisher(ApplicationEventPublisher publisher) {
    this.publisher = publisher;
  }

  @Override
  public void run(String... args) throws JobExecutionException, IOException {
    val jobParameters = getJobParameters(args);

    if (jobParameters == null) {
      throw new IllegalArgumentException("引数が指定されていません。");
    }

    // 引数のjobNameでJobインスタンスを取り出す
    String targetJobName = jobParameters.getString(JOB_PARAMETER_JOB_NAME);

    Optional<Job> jobOpt =
        jobs.stream().filter(s -> s.getName().equalsIgnoreCase(targetJobName)).findFirst();
    Job job =
        jobOpt.orElseThrow(
            () -> new IllegalArgumentException("指定されたジョブが見つかりません。[jobName=" + targetJobName + "]"));

    // 二重起動防止ファイルを作成する
    createProcessFile(processFileLocation, targetJobName);

    // ジョブを実行する
    val status = execute(job, jobParameters);

    // 二重起動防止ファイルを削除する
    deleteProcessFile(status, processFileLocation, targetJobName);
  }

  /**
   * パラメータを解析してJobParameters型に変換する。
   *
   * @param args
   * @return
   */
  protected JobParameters getJobParameters(String[] args) {
    val props = StringUtils.splitArrayElementsIntoProperties(args, "=");

    if (log.isDebugEnabled() && props != null) {
      props.entrySet().stream()
          .forEach(
              e -> log.debug("args: key={}, value={}", e.getKey(), String.valueOf(e.getValue())));
    }

    return converter.getJobParameters(props);
  }

  /**
   * 二重起動防止ファイルを作成する。
   *
   * @param processFileLocation
   * @param jobName
   */
  protected void createProcessFile(String processFileLocation, String jobName) throws IOException {
    // 二重起動防止ファイルの保存先を作成する
    val location = Paths.get(processFileLocation);
    FileUtils.createDirectory(location);

    // 二重起動防止ファイルを作成する
    val path = location.resolve(jobName);
    try {
      Files.createFile(path);
    } catch (FileAlreadyExistsException e) {
      log.error("二重起動防止ファイルが存在するため処理を中断します。[path={}]", path.toAbsolutePath().toString());
      throw e;
    }
  }

  /**
   * 二重起動防止ファイルを削除する。
   *
   * @param status
   * @param processFileLocation
   * @param jobName
   */
  protected void deleteProcessFile(BatchStatus status, String processFileLocation, String jobName)
      throws IOException {

    if (status == BatchStatus.COMPLETED) {
      val location = Paths.get(processFileLocation);
      val path = location.resolve(jobName);
      Files.deleteIfExists(path);
    }
  }

  protected BatchStatus execute(Job job, JobParameters jobParameters)
      throws JobExecutionAlreadyRunningException, JobRestartException,
          JobInstanceAlreadyCompleteException, JobParametersInvalidException,
          JobParametersNotFoundException {

    BatchStatus status = BatchStatus.UNKNOWN;
    val nextParameters = getNextJobParameters(job, jobParameters);

    if (nextParameters != null) {
      val execution = jobLauncher.run(job, nextParameters);

      if (publisher != null) {
        publisher.publishEvent(new JobExecutionEvent(execution));
      }

      status = execution.getStatus();
    }

    return status;
  }

  protected JobParameters getNextJobParameters(Job job, JobParameters parameters) {
    String name = job.getName();
    JobParameters mergeParameters = new JobParameters();
    val lastInstances = jobExplorer.getJobInstances(name, 0, 1);
    val incrementer = job.getJobParametersIncrementer();
    val additional = parameters.getParameters();

    if (lastInstances.isEmpty()) {
      // Start from a completely clean sheet
      if (incrementer != null) {
        mergeParameters = incrementer.getNext(new JobParameters());
      }
    } else {
      val previousExecutions = jobExplorer.getJobExecutions(lastInstances.get(0));
      val previousExecution = previousExecutions.get(0);

      if (previousExecution == null) {
        // Normally this will not happen - an instance exists with no executions
        if (incrementer != null) {
          mergeParameters = incrementer.getNext(new JobParameters());
        }
      } else if (isStoppedOrFailed(previousExecution) && job.isRestartable()) {
        // Retry a failed or stopped execution
        mergeParameters = previousExecution.getJobParameters();
        // Non-identifying additional parameters can be removed to a retry
        removeNonIdentifying(additional);
      } else if (incrementer != null) {
        // New instance so increment the parameters if we can
        mergeParameters = incrementer.getNext(previousExecution.getJobParameters());
      }
    }

    return merge(mergeParameters, additional);
  }

  protected boolean isStoppedOrFailed(JobExecution execution) {
    val status = execution.getStatus();
    return (status == BatchStatus.STOPPED || status == BatchStatus.FAILED);
  }

  protected void removeNonIdentifying(Map<String, JobParameter> parameters) {
    Map<String, JobParameter> copy = new HashMap<>(parameters);

    for (val parameter : copy.entrySet()) {
      if (!parameter.getValue().isIdentifying()) {
        parameters.remove(parameter.getKey());
      }
    }
  }

  protected JobParameters merge(JobParameters merged, Map<String, JobParameter> parameters) {
    Map<String, JobParameter> temp = new HashMap<>();
    temp.putAll(merged.getParameters());
    temp.putAll(parameters);
    merged = new JobParameters(temp);
    return merged;
  }
}
