package com.sample.domain.dto.common;

/**
 * MultipartFileインターフェースがwebモジュールに依存しているので、 <br>
 * 本インターフェースを介させることで循環参照にならないようにする。
 */
public interface MultipartFileConvertible {

  void setFilename(String filename);

  void setOriginalFilename(String originalFilename);

  void setContentType(String contentType);

  void setContent(BZip2Data data);
}
