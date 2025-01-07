package com.bigtreetc.sample.batch.jobs.sendmail;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

import com.bigtreetc.sample.batch.context.BatchContextHolder;
import com.bigtreetc.sample.batch.jobs.BaseTestContainerTest;
import com.bigtreetc.sample.domain.dao.AuditInfoHolder;
import com.bigtreetc.sample.domain.dao.SendMailQueueDao;
import com.bigtreetc.sample.domain.entity.SendMailQueue;
import java.time.LocalDateTime;
import java.util.List;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.support.TransactionTemplate;

@SpringBootTest
class SendMailJobTest extends BaseTestContainerTest {

  @Autowired
  @Qualifier("sendMailJob")
  Job job;

  @Autowired JobLauncher jobLauncher;

  @Autowired JobRepository jobRepository;

  @Autowired TransactionTemplate transactionTemplate;

  @Autowired SendMailQueueDao sendMailQueueDao;

  @Autowired TestMailSendJobDao testMailSendJobDao;

  JobLauncherTestUtils testUtils;

  @BeforeEach
  public void before() {
    testUtils = new JobLauncherTestUtils();
    testUtils.setJobLauncher(jobLauncher);
    testUtils.setJobRepository(jobRepository);
    testUtils.setJob(job);

    // 初期化
    BatchContextHolder.getContext().clear();

    // 監査情報の設定
    AuditInfoHolder.set("test", LocalDateTime.now());
  }

  @Test
  @DisplayName("メール送信バッチが起動すること")
  void test1() throws Exception {
    // メール送信キューにレコードを登録する
    transactionTemplate.execute(
        transactionStatus -> {
          val sendMailQueue = new SendMailQueue();
          sendMailQueue.setFrom("from@example.com");
          sendMailQueue.setTo("test1to@example.com");
          sendMailQueue.setSubject("subject");
          sendMailQueue.setBody("test");
          sendMailQueueDao.insert(List.of(sendMailQueue));
          return null;
        });

    val jobParameter = testUtils.getUniqueJobParameters();
    val execution = testUtils.getJobLauncher().run(job, jobParameter);

    val actual = execution.getStatus().name();
    val expected = BatchStatus.COMPLETED.name();
    assertThat(actual).isEqualTo(expected);

    val context = BatchContextHolder.getContext();
    val totalCount = context.getTotalCount();
    assertThat(totalCount).isEqualTo(1L);

    val mailList = testMailSendJobDao.selectAll("test1to@example.com", toList());
    assertThat(mailList).hasSize(1);
  }
}
