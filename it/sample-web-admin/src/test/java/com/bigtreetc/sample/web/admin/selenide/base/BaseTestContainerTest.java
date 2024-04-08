package com.bigtreetc.sample.web.admin.selenide.base;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.WebDriverRunner;
import java.time.Duration;
import lombok.val;
import org.junit.jupiter.api.*;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.env.Environment;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.Testcontainers;
import org.testcontainers.containers.BrowserWebDriverContainer;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;

public class BaseTestContainerTest {

  @LocalServerPort Integer port;

  static final ChromeOptions chrome =
      new ChromeOptions().addArguments("--no-sandbox").addArguments("--disable-dev-shm-usage");

  static final BrowserWebDriverContainer<?> BROWSER_CONTAINER =
      new BrowserWebDriverContainer<>("selenium/standalone-chrome")
          .withCapabilities(chrome)
          .waitingFor(Wait.forLogMessage(".*Started Selenium Standalone.*", 1));

  static final MySQLContainer<?> MYSQL_CONTAINER = new MySQLContainer<>("mysql:8.2");

  static final GenericContainer<?> MAILHOG_CONTAINER =
      new GenericContainer<>("mailhog/mailhog")
          .withExposedPorts(1025, 8025)
          .waitingFor(Wait.forHttp("/").forPort(8025));

  @BeforeAll
  static void beforeAll(@Autowired Environment environment) throws InterruptedException {
    val localServerPort = environment.getProperty("local.server.port", Integer.class);
    Testcontainers.exposeHostPorts(localServerPort);
    BROWSER_CONTAINER.start();
    Configuration.timeout = 30000;
    Configuration.pageLoadTimeout = 5000;
  }

  @BeforeEach
  void before() {
    Configuration.baseUrl = String.format("http://host.testcontainers.internal:%d", port);
    val driver = new RemoteWebDriver(BROWSER_CONTAINER.getSeleniumAddress(), chrome);
    driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(60));
    WebDriverRunner.setWebDriver(driver);
  }

  @AfterEach
  void after() {
    WebDriverRunner.closeWebDriver();
  }

  @AfterAll
  void afterAll() {
    BROWSER_CONTAINER.stop();
    MYSQL_CONTAINER.stop();
    MAILHOG_CONTAINER.stop();
  }

  @DynamicPropertySource
  static void overrideProperties(DynamicPropertyRegistry registry) {
    MYSQL_CONTAINER.start();
    MAILHOG_CONTAINER.start();

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
