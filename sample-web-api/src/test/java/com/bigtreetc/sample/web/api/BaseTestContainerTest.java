package com.bigtreetc.sample.web.api;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public class BaseTestContainerTest {

  private static final MySQLContainer<?> MYSQL_CONTAINER = new MySQLContainer<>("mysql:8");

  private static final GenericContainer<?> MAILHOG_CONTAINER =
      new GenericContainer<>("mailhog/mailhog")
          .withExposedPorts(1025, 8025)
          .waitingFor(Wait.forHttp("/").forPort(8025));

  static {
    MYSQL_CONTAINER.start();
    MAILHOG_CONTAINER.start();
  }

  @DynamicPropertySource
  static void overrideProperties(DynamicPropertyRegistry registry) {
    registry.add(
        "spring.datasource.url",
        () ->
            "jdbc:mysql://%s:%d/%s"
                .formatted(
                    MYSQL_CONTAINER.getHost(),
                    MYSQL_CONTAINER.getFirstMappedPort(),
                    MYSQL_CONTAINER.getDatabaseName()));
    registry.add("spring.datasource.username", MYSQL_CONTAINER::getUsername);
    registry.add("spring.datasource.password", MYSQL_CONTAINER::getPassword);
    registry.add("spring.flyway.url", MYSQL_CONTAINER::getJdbcUrl);
    registry.add("spring.flyway.user", MYSQL_CONTAINER::getUsername);
    registry.add("spring.flyway.password", MYSQL_CONTAINER::getPassword);
    registry.add("spring.mail.host", MAILHOG_CONTAINER::getHost);
    registry.add("spring.mail.port", MAILHOG_CONTAINER::getFirstMappedPort);
  }
}
