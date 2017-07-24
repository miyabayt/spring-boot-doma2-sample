package com.sample.web.base.controller.api.resource;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ErrorResourceImpl extends ResourceImpl {

    // リクエストID
    private String requestId;

    // 入力エラー
    private List<FieldErrorResource> fieldErrors;

    public ErrorResourceImpl() {
    }
}
