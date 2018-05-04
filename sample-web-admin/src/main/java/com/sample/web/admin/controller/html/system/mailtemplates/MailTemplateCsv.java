package com.sample.web.admin.controller.html.system.mailtemplates;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true) // 定義されていないプロパティを無視してマッピングする
@JsonPropertyOrder({ "メールテンプレートID", "メールテンプレートキー" }) // CSVのヘッダ順
@Getter
@Setter
public class MailTemplateCsv implements Serializable {

    private static final long serialVersionUID = 3277131881879633731L;

    @JsonProperty("メールテンプレートID")
    Long id;

    @JsonProperty("メールテンプレートキー")
    String templateKey;

    @JsonProperty("メールタイトル")
    String subject;

    @JsonProperty("メール本文")
    String templateBody;
}
