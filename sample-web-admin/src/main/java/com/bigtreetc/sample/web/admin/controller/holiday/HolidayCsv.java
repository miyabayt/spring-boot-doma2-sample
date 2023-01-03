package com.bigtreetc.sample.web.admin.controller.holiday;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.io.Serializable;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true) // 定義されていないプロパティを無視してマッピングする
@JsonPropertyOrder({"祝日ID", "祝日名", "日付"}) // CSVのヘッダ順
@Getter
@Setter
public class HolidayCsv implements Serializable {

  private static final long serialVersionUID = 6658799113183356993L;

  @JsonProperty("祝日ID")
  Long id;

  @JsonProperty("祝日名")
  String holidayName;

  @JsonProperty("日付")
  @JsonFormat(pattern = "yyyy/MM/dd")
  LocalDate holidayDate;
}
