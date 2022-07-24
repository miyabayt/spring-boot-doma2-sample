package com.sample.web.base.controller.api.resource;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ErrorResourceImpl extends ResourceImpl {

  // リクエストID
  String requestId;

  // 入力エラー
  List<FieldErrorResource> fieldErrors;

  public ErrorResourceImpl() {}
}
