package com.sample.web.base.controller.api.resource;

import java.util.List;

public interface Resource {

  List<?> getContent();

  void setContent(List<?> content);

  String getMessage();

  void setMessage(String message);
}
