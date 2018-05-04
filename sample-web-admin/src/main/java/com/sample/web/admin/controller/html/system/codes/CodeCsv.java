package com.sample.web.admin.controller.html.system.codes;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true) // 定義されていないプロパティを無視してマッピングする
@JsonPropertyOrder({ "コードID", "コード分類キー", "コード分類名", "コードキー", "コード値", "コードエイリアス", "表示順", "無効フラグ" }) // CSVのヘッダ順
@Getter
@Setter
public class CodeCsv implements Serializable {

    private static final long serialVersionUID = -3895412714445561940L;

    @JsonProperty("コードID")
    Long id;

    @JsonProperty("コード分類キー")
    String categoryKey;

    @JsonProperty("コード分類名")
    String categoryName;

    @JsonProperty("コードキー")
    String codeKey;

    @JsonProperty("コード値")
    String codeValue;

    @JsonProperty("コードエイリアス")
    String codeAlias;

    @JsonProperty("表示順")
    Integer displayOrder;

    @JsonProperty("無効フラグ")
    Boolean isInvalid;
}
