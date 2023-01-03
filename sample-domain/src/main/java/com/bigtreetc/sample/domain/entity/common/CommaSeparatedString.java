package com.bigtreetc.sample.domain.entity.common;

import java.io.Serializable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.val;
import org.seasar.doma.Domain;

@Domain(valueType = String.class, factoryMethod = "of")
@NoArgsConstructor
public class CommaSeparatedString implements Serializable {

  private static final long serialVersionUID = -6864852815920199569L;

  private static final String COMMA = ",";

  @Getter String data;

  /**
   * ファクトリメソッド
   *
   * @param data
   * @return
   */
  public static CommaSeparatedString of(String data) {
    val css = new CommaSeparatedString();
    css.data = String.join(",", data);
    return css;
  }

  public String[] getSplitedString() {
    if (this.data == null) {
      return null;
    }
    return this.data.split(COMMA);
  }

  // ResultSet.getBytes(int)で取得された値がこのコンストラクタで設定される
  CommaSeparatedString(String data) {
    this.data = String.join(COMMA, data);
  }

  // PreparedStatement.setBytes(int, bytes)へ設定する値がこのメソッドから取得される
  String getValue() {
    return this.data;
  }
}
