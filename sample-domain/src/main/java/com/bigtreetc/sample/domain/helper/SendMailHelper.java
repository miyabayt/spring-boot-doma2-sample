package com.bigtreetc.sample.domain.helper;

import com.bigtreetc.sample.common.util.ValidateUtils;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.templateresolver.ITemplateResolver;
import org.thymeleaf.templateresolver.StringTemplateResolver;

/** メール送信ヘルパー */
@Component
@Slf4j
public class SendMailHelper {

  @Autowired JavaMailSender javaMailSender;

  /**
   * メールを送信します。
   *
   * @param fromAddress
   * @param toAddress
   * @param subject
   * @param body
   */
  public void sendMail(String fromAddress, String toAddress, String subject, String body) {
    this.sendMail(fromAddress, new String[] {toAddress}, null, null, subject, body);
  }

  /**
   * メールを送信します。
   *
   * @param fromAddress
   * @param toAddress
   * @param ccAddress
   * @param bccAddress
   * @param subject
   * @param body
   */
  public void sendMail(
      String fromAddress,
      String[] toAddress,
      String[] ccAddress,
      String[] bccAddress,
      String subject,
      String body) {
    val message = new SimpleMailMessage();
    message.setFrom(fromAddress);
    message.setTo(toAddress);
    if (ccAddress != null && ccAddress.length > 0) {
      message.setCc(ccAddress);
    }
    if (bccAddress != null && bccAddress.length > 0) {
      message.setBcc(bccAddress);
    }
    message.setSubject(subject);
    message.setText(body);

    try {
      javaMailSender.send(message);
    } catch (MailException e) {
      log.error("failed toAddress send mail.", e);
      throw e;
    }
  }

  /**
   * 指定したテンプレートのメール本文を返します。
   *
   * @param template
   * @param objects
   * @return
   */
  public String getMailBody(String template, Map<String, Object> objects) {
    val templateEngine = new SpringTemplateEngine();
    templateEngine.setTemplateResolver(templateResolver());

    val context = new Context();
    if (ValidateUtils.isNotEmpty(objects)) {
      objects.forEach(context::setVariable);
    }

    return templateEngine.process(template, context);
  }

  protected ITemplateResolver templateResolver() {
    val resolver = new StringTemplateResolver();
    resolver.setTemplateMode("TEXT");
    resolver.setCacheable(false); // 安全をとってキャッシュしない
    return resolver;
  }
}
