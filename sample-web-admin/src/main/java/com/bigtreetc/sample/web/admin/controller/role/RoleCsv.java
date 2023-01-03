package com.bigtreetc.sample.web.admin.controller.role;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true) // 定義されていないプロパティを無視してマッピングする
@JsonPropertyOrder({"ロールID", "ロールコード", "ロール名"}) // CSVのヘッダ順
@Getter
@Setter
public class RoleCsv implements Serializable {

  private static final long serialVersionUID = -3895412714445561940L;

  @JsonProperty("ロールID")
  Long id;

  @JsonProperty("ロールコード")
  String roleCode;

  @JsonProperty("ロール名")
  String roleName;
}
