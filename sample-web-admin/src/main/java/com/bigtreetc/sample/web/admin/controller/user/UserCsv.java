package com.bigtreetc.sample.web.admin.controller.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true) // 定義されていないプロパティを無視してマッピングする
@JsonPropertyOrder({"ユーザーID", "姓", "名", "メールアドレス", "電話番号", "郵便番号", "住所"}) // CSVのヘッダ順
@Getter
@Setter
public class UserCsv implements Serializable {

  private static final long serialVersionUID = -1883999589975469540L;

  @JsonProperty("ユーザーID")
  Long id;

  // ハッシュ化されたパスワード
  @JsonIgnore // CSVに出力しない
  String password;

  @JsonProperty("名")
  String firstName;

  @JsonProperty("姓")
  String lastName;

  @JsonProperty("メールアドレス")
  String email;

  @JsonProperty("電話番号")
  String tel;

  @JsonProperty("郵便番号")
  String zip;

  @JsonProperty("住所")
  String address;
}
