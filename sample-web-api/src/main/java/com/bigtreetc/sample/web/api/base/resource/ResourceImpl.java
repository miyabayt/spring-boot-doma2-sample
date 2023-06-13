package com.bigtreetc.sample.web.api.base.resource;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResourceImpl implements Resource {

  List<?> content;

  // メッセージ
  String message;
}
