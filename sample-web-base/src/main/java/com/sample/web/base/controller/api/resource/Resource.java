package com.sample.web.base.controller.api.resource;

import com.sample.domain.dto.common.Dto;
import java.util.List;

public interface Resource {

  List<? extends Dto> getData();

  void setData(List<? extends Dto> data);

  String getMessage();

  void setMessage(String message);
}
