package com.sample.web.base.validator.annotation;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.web.multipart.MultipartFile;

import lombok.val;

/**
 * 入力チェック（コンテンツタイプ）
 */
public class ContentTypeValidator implements ConstraintValidator<ContentType, MultipartFile> {

    private List<String> allowed = new ArrayList<>();
    private List<String> rejected = new ArrayList<>();

    @Override
    public void initialize(ContentType fileExtension) {
        Stream.of(fileExtension.allowed()).forEach(a -> allowed.add(a));
        Stream.of(fileExtension.rejected()).forEach(r -> rejected.add(r));
    }

    @Override
    public boolean isValid(MultipartFile value, ConstraintValidatorContext context) {
        boolean isValid = false;

        try {
            if (value == null || value.isEmpty()) {
                return true;
            }

            val contentType = value.getContentType();

            if (rejected.contains(contentType)) {
                isValid = false;
            } else if (allowed.contains(contentType)) {
                isValid = true;
            }
        } catch (final Exception ignore) {
            // ignore
        }

        return isValid;
    }
}
