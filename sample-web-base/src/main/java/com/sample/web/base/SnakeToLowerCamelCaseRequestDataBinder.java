package com.sample.web.base;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletRequest;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.WordUtils;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.web.servlet.mvc.method.annotation.ExtendedServletRequestDataBinder;

public class SnakeToLowerCamelCaseRequestDataBinder extends ExtendedServletRequestDataBinder {

  /**
   * コンストラクタ
   *
   * @param target
   * @param objectName
   */
  public SnakeToLowerCamelCaseRequestDataBinder(Object target, String objectName) {
    super(target, objectName);
  }

  @Override
  protected void addBindValues(MutablePropertyValues mpvs, ServletRequest request) {
    super.addBindValues(mpvs, request);

    for (val from : getPropertyNames(mpvs)) {
      val to = toCamelCase(from);
      if (mpvs.contains(from)) {
        mpvs.add(to, mpvs.getPropertyValue(from).getValue());
      }
    }
  }

  /**
   * プロパティ名を取得します。
   *
   * @param mpvs
   * @return
   */
  private List<String> getPropertyNames(MutablePropertyValues mpvs) {
    val list = new ArrayList<String>();
    for (val pv : mpvs.getPropertyValueList()) {
      list.add(pv.getName());
    }
    return list;
  }

  /**
   * 引数を LowerCamelCase に変換して返します。
   *
   * @param from
   * @return
   */
  private String toCamelCase(String from) {
    val camel = StringUtils.remove(WordUtils.capitalizeFully(from, '_'), "_");

    if (camel == null || camel.length() < 2) {
      return camel;
    }

    return camel.substring(0, 1).toLowerCase() + camel.substring(1);
  }
}
