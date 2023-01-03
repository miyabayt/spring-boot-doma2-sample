package com.bigtreetc.sample.common.util;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Predicate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;

@Slf4j
public class EnvironmentUtils {

  private static boolean isLocalOrTest = false;

  public static void init(Environment environment) {
    Predicate<String> isLocal = profile -> Objects.equals(profile, "local");
    Predicate<String> isTest = profile -> Objects.equals(profile, "test");

    boolean isLocalOrTest;
    if (environment.getActiveProfiles().length > 0) {
      isLocalOrTest = Arrays.stream(environment.getActiveProfiles()).anyMatch(isLocal.or(isTest));
    } else {
      isLocalOrTest = true;
    }

    log.info("isLocalOrTest: {}", isLocalOrTest);
    EnvironmentUtils.isLocalOrTest = isLocalOrTest;
  }

  public static boolean isLocalOrTest() {
    return EnvironmentUtils.isLocalOrTest;
  }
}
