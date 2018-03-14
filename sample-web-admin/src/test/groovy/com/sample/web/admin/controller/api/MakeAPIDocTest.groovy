package com.sample.web.admin.controller.api

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import org.springframework.web.context.WebApplicationContext
import spock.lang.Shared
import spock.lang.Specification
import springfox.documentation.staticdocs.Swagger2MarkupResultHandler

import static org.hamcrest.Matchers.containsString
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup

@SpringBootTest
class MakeAPIDocTest extends Specification {

    @Autowired
    WebApplicationContext wac

    @Shared
    MockMvc mvc

    def setup() {
        mvc = webAppContextSetup(wac).apply(springSecurity()).build()
    }

    @WithMockUser(username = "test@sample.com", password = "passw0rd", authorities = ".*")
    def "自動生成されたSwagger SpecificationからAPIドキュメントを生成（asciidoc形式）"() {
        expect:
        mvc.perform(get("/v2/api-docs?group=api")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(Swagger2MarkupResultHandler.outputDirectory("build/asciidoc/snippets").build())
                .andExpect(status().isOk())
    }
}
