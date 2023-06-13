package com.bigtreetc.sample.web.api.base.resource;

import java.util.List;

public interface Resource {

  List<?> getContent();

  void setContent(List<?> content);

  String getMessage();

  void setMessage(String message);
}
