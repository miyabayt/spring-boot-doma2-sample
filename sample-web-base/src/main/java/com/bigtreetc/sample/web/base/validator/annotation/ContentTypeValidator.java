package com.bigtreetc.sample.web.base.validator.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.val;
import org.springframework.web.multipart.MultipartFile;

/** 入力チェック（コンテンツタイプ） */
public class ContentTypeValidator implements ConstraintValidator<ContentType, MultipartFile> {

  private List<String> allowed = new ArrayList<>();
  private List<String> rejected = new ArrayList<>();

  @Override
  public void initialize(ContentType fileExtension) {
    allowed.addAll(Arrays.asList(fileExtension.allowed()));
    rejected.addAll(Arrays.asList(fileExtension.rejected()));
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
