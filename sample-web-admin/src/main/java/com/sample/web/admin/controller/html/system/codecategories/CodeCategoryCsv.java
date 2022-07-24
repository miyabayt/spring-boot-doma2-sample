package com.sample.web.admin.controller.html.system.codecategories;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true) // 定義されていないプロパティを無視してマッピングする
@JsonPropertyOrder({"コードID", "コード分類キー", "コード分類名"}) // CSVのヘッダ順
@Getter
@Setter
public class CodeCategoryCsv implements Serializable {

  private static final long serialVersionUID = -1235021910126027275L;

  @JsonProperty("コード分類ID")
  Long id;

  @JsonProperty("コード分類キー")
  String categoryKey;

  @JsonProperty("コード分類名")
  String categoryName;
}
