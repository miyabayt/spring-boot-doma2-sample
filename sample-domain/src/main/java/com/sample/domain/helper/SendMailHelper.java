package com.sample.domain.helper;

import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.templateresolver.ITemplateResolver;
import org.thymeleaf.templateresolver.StringTemplateResolver;

import lombok.val;
import lombok.extern.slf4j.Slf4j;

/**
 * メール送信ヘルパー
 */
@Component
@Slf4j
public class SendMailHelper {

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
     * @param objects
     * @return
     */
    public String getMailBody(String template, Map<String, Object> objects) {
        val templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(templateResolver());

        val context = new Context();
        if (MapUtils.isNotEmpty(objects)) {
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
