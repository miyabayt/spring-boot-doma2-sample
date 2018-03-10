package com.sample.web.admin.controller.api.v1.user

import com.sample.domain.dto.common.ID
import com.sample.domain.service.user.UserService
import org.junit.Rule
import org.spockframework.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.restdocs.JUnitRestDocumentation
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.context.WebApplicationContext;
import spock.lang.Shared;
import spock.lang.Specification
import com.sample.domain.dto.user.User

import javax.swing.plaf.basic.BasicInternalFrameTitlePane
import java.time.LocalDateTime;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;

@SpringBootTest
class UserRestControllerTest extends Specification {

    @Rule
    public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("build/generated-snippets")

    @Autowired
    WebApplicationContext wac

    @Autowired
    UserService userService;

    @Shared
    MockMvc mvc

    def setup() {
        mvc = webAppContextSetup(wac)
                .apply(springSecurity())
                .apply(documentationConfiguration(restDocumentation))
                .alwaysDo(document("user",
                        pathParameters(
                            parameterWithName("userId").description("ユーザID")
                        ),
                        requestHeaders(
                                headerWithName("Authorization").description(
                                        "Basic認証")),
                        responseHeaders(
                                headerWithName("X-Track-Id").description(
                                        "リクエスト追跡ID"))
                        ))
                .build()
    }

    def "リクエストしたユーザが存在する場合、ユーザが取得できて正常終了する"() {
        given:
            Assert.notNull(userService.findById(new ID(1)))

        when:
            ResultActions actions =
                    mvc.perform(get("/api/v1/users/{userId}","1").with(httpBasic("test@sample.com","passw0rd")))

        then:
            actions
                    .andExpect(status().is(200))
                    .andExpect(jsonPath("\$.data[0].email").value("test@sample.com"))
                    .andExpect(jsonPath("\$.message").value("正常終了"))
                    .andDo(document("user"))
    }
}
