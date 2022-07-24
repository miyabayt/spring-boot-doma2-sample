package com.sample.web.admin.controller.html.system.roles;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true) // 定義されていないプロパティを無視してマッピングする
@JsonPropertyOrder({"役割ID", "役割キー", "役割名"}) // CSVのヘッダ順
@Getter
@Setter
public class RoleCsv implements Serializable {

  private static final long serialVersionUID = -3895412714445561940L;

  @JsonProperty("役割ID")
  Long id;

  @JsonProperty("役割キー")
  String roleKey;

  @JsonProperty("役割名")
  String roleName;
}
