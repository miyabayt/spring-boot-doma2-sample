package com.bigtreetc.sample.web.admin.selenide;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

import com.bigtreetc.sample.web.admin.Application;
import com.bigtreetc.sample.web.admin.selenide.base.BaseTestContainerTest;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(
    classes = Application.class,
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ShowTopTest extends BaseTestContainerTest {

  @Test
  @DisplayName("トップページを表示するシナリオ")
  public void test1() {
    open("/admin/login");
    $(By.name("loginId")).val("test@sample.com");
    $(By.name("password")).val("passw0rd");
    $("[type=submit]").click();

    $(".alert").shouldHave(text("ログインしました"));
  }
}
