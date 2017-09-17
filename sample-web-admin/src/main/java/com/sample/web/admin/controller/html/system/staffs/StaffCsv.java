package com.sample.web.admin.controller.html.system.staffs;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true) // 定義されていないプロパティを無視してマッピングする
@JsonPropertyOrder({ "担当者ID", "苗字", "名前", "メールアドレス", "電話番号" }) // CSVのヘッダ順
@Getter
@Setter
public class StaffCsv implements Serializable {

    private static final long serialVersionUID = -1883999589975469540L;

    @JsonProperty("担当者ID")
    Integer id;

    // ハッシュ化されたパスワード
    @JsonIgnore // CSVに出力しない
    String password;

    @JsonProperty("名前")
    String firstName;

    @JsonProperty("苗字")
    String lastName;

    @JsonProperty("メールアドレス")
    String email;

    @JsonProperty("電話番号")
    String tel;
}
