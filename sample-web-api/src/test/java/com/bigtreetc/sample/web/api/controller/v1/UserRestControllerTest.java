package com.bigtreetc.sample.web.api.controller.v1;

import static com.bigtreetc.sample.web.api.WebApiConst.ACCESS_DENIED_ERROR;
import static com.bigtreetc.sample.web.base.WebConst.MESSAGE_SUCCESS;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.bigtreetc.sample.common.util.MessageUtils;
import com.bigtreetc.sample.web.api.BaseTestContainerTest;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
class UserRestControllerTest extends BaseTestContainerTest {

  @Autowired WebApplicationContext wac;

  MockMvc mockMvc;

  @BeforeEach
  public void setup() {
    mockMvc = MockMvcBuilders.webAppContextSetup(wac).apply(springSecurity()).build();
  }

  @Test
  @DisplayName("権限を持つロールで、顧客マスタを検索できること")
  @WithMockUser(authorities = "user:read")
  void test1() throws Exception {
    val message = MessageUtils.getMessage(MESSAGE_SUCCESS);

    mockMvc
        .perform(get("/api/v1/users").contextPath("/api"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.message").value(message))
        .andExpect(jsonPath("$.content").isArray());
  }

  @Test
  @DisplayName("権限を持たないロールでは、顧客マスタを検索できないこと")
  @WithMockUser(authorities = "user:dummy")
  void test2() throws Exception {
    val message = MessageUtils.getMessage(ACCESS_DENIED_ERROR);

    mockMvc
        .perform(get("/api/v1/users").contextPath("/api"))
        .andExpect(status().isForbidden())
        .andExpect(jsonPath("$.message").value(message));
  }
}
