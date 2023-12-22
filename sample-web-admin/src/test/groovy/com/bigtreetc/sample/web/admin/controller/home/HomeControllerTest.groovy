package com.bigtreetc.sample.web.admin.controller.home

import com.bigtreetc.sample.domain.BaseTestContainerSpec
import org.flywaydb.test.annotation.FlywayTest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import org.springframework.web.context.WebApplicationContext
import spock.lang.Shared

import java.nio.charset.StandardCharsets

import static org.hamcrest.Matchers.containsString
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup

@FlywayTest
@SpringBootTest
class HomeControllerTest extends BaseTestContainerSpec {

    @Autowired
    WebApplicationContext wac

    @Shared
    MockMvc mvc

    def setup() {
        mvc = webAppContextSetup(wac)
                .apply(springSecurity())
                .defaultResponseCharacterEncoding(StandardCharsets.UTF_8)
                .build()
    }

    @WithMockUser(username = "test@sample.com", password = "passw0rd", roles = ["system_admin"])
    def "権限を持つ担当者でホームを開けること"() {
        expect:
        mvc.perform(get("/home"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("ホーム | Sample")))
                .andExpect(model().hasNoErrors())
    }

    @WithMockUser(username = "test@sample.com", password = "passw0rd", roles = ["user"])
    def "権限を持たない担当者でホームが開けないこと"() {
        expect:
        mvc.perform(get("/home"))
                .andExpect(status().isForbidden())
                .andExpect(content().string(containsString("権限が不足しています")))
    }
}
