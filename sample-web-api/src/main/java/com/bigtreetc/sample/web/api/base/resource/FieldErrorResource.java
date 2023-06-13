package com.bigtreetc.sample.web.api.base.resource;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FieldErrorResource {

  // 項目名
  String fieldName;

  // エラー種別
  String errorType;

  // エラーメッセージ
  String errorMessage;
}
