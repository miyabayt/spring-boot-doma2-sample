package com.bigtreetc.sample.batch.jobs.birthdayMail;

import com.bigtreetc.sample.batch.context.BatchContext;
import com.bigtreetc.sample.batch.jobs.BaseItemProcessor;
import com.bigtreetc.sample.domain.dao.MailTemplateDao;
import com.bigtreetc.sample.domain.entity.MailTemplate;
import com.bigtreetc.sample.domain.entity.MailTemplateCriteria;
import com.bigtreetc.sample.domain.entity.SendMailQueue;
import com.bigtreetc.sample.domain.entity.User;
import com.bigtreetc.sample.domain.entity.common.CommaSeparatedString;
import com.bigtreetc.sample.domain.exception.NoDataFoundException;
import com.bigtreetc.sample.domain.helper.SendMailHelper;
import java.util.HashMap;
import java.util.Map;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

public class BirthdayMailProcessor extends BaseItemProcessor<User, SendMailQueue> {

  @Value("${spring.mail.properties.mail.from}")
  String fromAddress;

  @Autowired MailTemplateDao mailTemplateDao;

  @Autowired SendMailHelper sendMailHelper;

  @Override
  protected void onValidationError(BatchContext context, BindingResult result, User item) {}

  @Override
  protected SendMailQueue doProcess(BatchContext context, User user) {
    val mailTemplate = getMailTemplate("birthdayMail");

    val subject = mailTemplate.getSubject();
    val templateBody = mailTemplate.getTemplateBody();

    Map<String, Object> objects = new HashMap<>();
    objects.put("user", user);

    val body = sendMailHelper.getMailBody(templateBody, objects);
    val to = CommaSeparatedString.of(user.getEmail());

    val transform = new SendMailQueue();
    transform.setFrom(fromAddress);
    transform.setTo(to);
    transform.setSubject(subject);
    transform.setBody(body);

    return transform;
  }

  /**
   * メールテンプレートを取得する。
   *
   * @return
   */
  protected MailTemplate getMailTemplate(String templateCode) {
    val criteria = new MailTemplateCriteria();
    criteria.setTemplateCode(templateCode);
    val mailTemplate =
        mailTemplateDao
            .select(criteria)
            .orElseThrow(
                () ->
                    new NoDataFoundException(
                        "templateCode=" + criteria.getTemplateCode() + " のデータが見つかりません。"));

    return mailTemplate;
  }

  @Override
  protected Validator getValidator() {
    return null;
  }
}
