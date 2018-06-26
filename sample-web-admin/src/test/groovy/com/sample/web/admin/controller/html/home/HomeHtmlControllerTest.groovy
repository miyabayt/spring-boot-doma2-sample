package com.sample.web.admin.controller.html.home

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import org.springframework.web.context.WebApplicationContext
import spock.lang.Shared
import spock.lang.Specification

import static org.hamcrest.Matchers.containsString
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup

@SpringBootTest
class HomeHtmlControllerTest extends Specification {

    @Autowired
    WebApplicationContext wac

    @Shared
    MockMvc mvc

    def setup() {
        mvc = webAppContextSetup(wac).apply(springSecurity()).build()
    }

    @WithMockUser(username = "test@sample.com", password = "passw0rd", authorities = ".*")
    def "権限を持つ担当者でホームを開けること"() {
        expect:
        mvc.perform(get("/home"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Home | Sample")))
                .andExpect(model().hasNoErrors())
    }

    @WithMockUser(username = "test@sample.com", password = "passw0rd", authorities = "test")
    def "権限を持たない担当者でホームが開けないこと"() {
        expect:
        mvc.perform(get("/home"))
                .andExpect(redirectedUrl("/forbidden"))
    }
}
