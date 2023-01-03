package com.bigtreetc.sample.web.base.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.type.CollectionLikeType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import java.io.Writer;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JacksonUtils {

  private static final ObjectMapper OBJECT_MAPPER =
      JsonMapper.builder()
          .findAndAddModules()
          .configure(MapperFeature.DEFAULT_VIEW_INCLUSION, true)
          .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
          .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
          .build();

  public static TypeFactory getTypeFactory() {
    return OBJECT_MAPPER.getTypeFactory();
  }

  @SneakyThrows
  public static <T> T readValue(String content, Class<T> valueType) {
    return OBJECT_MAPPER.readValue(content, valueType);
  }

  @SneakyThrows
  public static <T> T readValue(String content, TypeReference<T> valueTypeRef) {
    return OBJECT_MAPPER.readValue(content, valueTypeRef);
  }

  @SneakyThrows
  public static <T> T readValue(String content, CollectionLikeType collectionLikeType) {
    return OBJECT_MAPPER.readValue(content, collectionLikeType);
  }

  @SneakyThrows
  public static void writeValue(Writer writer, Object value) {
    OBJECT_MAPPER.writeValue(writer, value);
  }

  @SneakyThrows
  public static byte[] writeValueAsBytes(Object value) {
    return OBJECT_MAPPER.writeValueAsBytes(value);
  }

  @SneakyThrows
  public static String writeValueAsString(Object value) {
    return OBJECT_MAPPER.writeValueAsString(value);
  }
}
