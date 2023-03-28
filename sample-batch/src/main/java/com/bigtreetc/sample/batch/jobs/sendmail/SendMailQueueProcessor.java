package com.bigtreetc.sample.batch.jobs.sendmail;

import com.bigtreetc.sample.batch.context.BatchContext;
import com.bigtreetc.sample.batch.jobs.BaseItemProcessor;
import com.bigtreetc.sample.domain.entity.SendMailQueue;
import com.bigtreetc.sample.domain.helper.SendMailHelper;
import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

@Slf4j
public class SendMailQueueProcessor extends BaseItemProcessor<SendMailQueue, SendMailQueue> {

  @Autowired SendMailHelper sendMailHelper;

  @Override
  protected void onValidationError(
      BatchContext context, BindingResult result, SendMailQueue item) {}

  @Override
  protected SendMailQueue doProcess(BatchContext context, SendMailQueue sendMailQueue) {
    val from = sendMailQueue.getFrom();
    val to = sendMailQueue.getTo();
    val cc = sendMailQueue.getCc();
    val bcc = sendMailQueue.getBcc();
    val subject = sendMailQueue.getSubject();
    val body = sendMailQueue.getBody();
    try {
      sendMailHelper.sendMail(from, to, cc, bcc, subject, body);
      sendMailQueue.setSentAt(LocalDateTime.now());
      context.increaseProcessCount();
    } catch (Exception e) {
      // skip
      context.increaseErrorCount();
      log.warn("cloud not send mail. [id={}]", sendMailQueue.getId());
      return null;
    }
    return sendMailQueue;
  }

  @Override
  protected Validator getValidator() {
    return null;
  }
}
