package com.bigtreetc.sample.web.admin.controller.codecategory;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true) // 定義されていないプロパティを無視してマッピングする
@JsonPropertyOrder({"コードID", "分類コード", "分類名"}) // CSVのヘッダ順
@Getter
@Setter
public class CodeCategoryCsv implements Serializable {

  private static final long serialVersionUID = -1235021910126027275L;

  @JsonProperty("コード分類ID")
  Long id;

  @JsonProperty("分類コード")
  String categoryCode;

  @JsonProperty("分類名")
  String categoryName;
}
