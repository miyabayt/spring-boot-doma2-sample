package com.sample.batch.jobs;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.listener.StepExecutionListenerSupport;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class BaseStepExecutionListener extends StepExecutionListenerSupport {

    @Override
    public void beforeStep(StepExecution stepExecution) {
        String stepName = stepExecution.getStepName();
        log.info("Step:{} ---- START ----", stepName);
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        String stepName = stepExecution.getStepName();
        log.info("Step:{} ---- END ----", stepName);

        ExitStatus exitStatus = stepExecution.getExitStatus();
        return exitStatus;
    }
}
