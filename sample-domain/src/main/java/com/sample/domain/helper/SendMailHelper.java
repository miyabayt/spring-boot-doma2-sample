package com.sample.domain.helper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import lombok.val;
import lombok.extern.slf4j.Slf4j;

/**
 * メール送信ヘルパー
 */
@Component
@Slf4j
public class SendMailHelper {

    @Value("${application.mailTemplateLocation}")
    String mailTemplateLocation;

    @Autowired
    JavaMailSender javaMailSender;

    /**
     * メールを送信します。
     * 
     * @param fromAddress
     * @param toAddress
     * @param subject
     * @param body
     */
    public void sendMail(String fromAddress, String[] toAddress, String subject, String body) {
        val message = new SimpleMailMessage();
        message.setFrom(fromAddress);
        message.setTo(toAddress);
        message.setSubject(subject);
        message.setText(body);

        try {
            javaMailSender.send(message);
        } catch (MailException e) {
            log.error("failed to send mail.", e);
            throw e;
        }
    }

    /**
     * 指定したテンプレートのメール本文を返します。
     *
     * @param template
     * @param objectName
     * @param object
     * @return
     */
    public String getMailBody(String template, String objectName, Object object) {
        val templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(emailTemplateResolver());

        val context = new Context();
        context.setVariable(objectName, object);

        String result = templateEngine.process(template, context);
        return result;
    }

    protected ClassLoaderTemplateResolver emailTemplateResolver() {
        Assert.notNull(mailTemplateLocation, "mailTemplateLocation must not be null");

        val resolver = new ClassLoaderTemplateResolver();
        resolver.setTemplateMode("HTML");
        resolver.setPrefix(mailTemplateLocation);
        resolver.setSuffix(".html");
        resolver.setCharacterEncoding("UTF-8");
        resolver.setCacheable(false); // 安全をとってキャッシュしない
        return resolver;
    }
}
