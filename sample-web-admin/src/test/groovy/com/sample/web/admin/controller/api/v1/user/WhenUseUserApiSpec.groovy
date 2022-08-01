package com.sample.web.admin.controller.api.v1.user

import com.sample.domain.BaseTestContainerSpec
import com.sample.web.admin.test.helper.Doma2TestHelper
import groovy.json.JsonSlurper
import org.flywaydb.test.annotation.FlywayTest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import org.springframework.web.context.WebApplicationContext
import spock.lang.Shared

import java.nio.charset.StandardCharsets

import static org.hamcrest.CoreMatchers.containsString
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup

@FlywayTest
@SpringBootTest
class WhenUseUserApiSpec extends BaseTestContainerSpec {

    @Autowired
    WebApplicationContext wac

    @Shared
    MockMvc mvc

    @Autowired
    Doma2TestHelper h

    def setup() {
        mvc = webAppContextSetup(wac)
                .apply(springSecurity())
                .defaultResponseCharacterEncoding(StandardCharsets.UTF_8)
                .build()
        h.delete "users"
    }

    @WithMockUser(username = "test@sample.com", password = "passw0rd", roles = ["system_admin"])
    def "ユーザAPIを使うシナリオ"() {
        given: "ユーザが１件もない状態で"
        when:
        def json = ユーザの一覧を取得する()

        then: "0件であるべき"
        json == '{"content":[],"message":"正常終了","page":1,"totalPages":0}'

        when:
        ユーザを追加する(asJson(["firstName": "ichiro"] <<
                ["lastName": "suzuki"] <<
                ["zip": "1500001"] <<
                ["address": "shibuyaku tokyo"]))
        and:
        json = ユーザの一覧を取得する()
        then: "追加したユーザが取得できるべき"
        json.contains('"message":"正常終了"')
        json.contains('"firstName":"ichiro","lastName":"suzuki"')

        when:
        def user = 追加したユーザを特定する(json)
        def id = user['id']
        def version = user['version']
        and:
        json = ユーザを取得する(id)
        then: "追加したユーザが取得できるべき"
        json.contains('"message":"正常終了"')
        json.contains('"firstName":"ichiro","lastName":"suzuki"')

        when:
        ユーザを更新する(id, asJson(["firstName": "goro"] <<
                ["lastName": "yamada"] <<
                ["zip": "1500001"] <<
                ["address": "shibuyaku tokyo"] <<
                ["version": version]))
        and:
        json = ユーザを取得する(id)
        then: "ユーザの情報が更新されているべき"
        json.contains('"message":"正常終了"')
        json.contains('"firstName":"goro","lastName":"yamada"')

        when:
        json = ユーザを削除する(id)
        and:
        json = ユーザの一覧を取得する()
        then: "0件であるべき"
        json == '{"content":[],"message":"正常終了","page":1,"totalPages":0}'
    }

    def asJson(map) {
        def s = map.collect { "\"${it.key}\": \"${it.value}\"" }
                .inject { s, v -> s + ',' + v }
        '{' + s + '}'
    }

    def "ユーザを追加する"(json) {
        mvc.perform(post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString('"message":"正常終了"')))
    }

    def "ユーザの一覧を取得する"() {
        mvc.perform(get("/api/v1/users"))
                .andExpect(status().isOk())
                .andReturn().response.getContentAsString()
    }

    def "追加したユーザを特定する"(jsonAsString) {
        def jsonSlurper = new JsonSlurper()
        def json = jsonSlurper.parseText(jsonAsString)
        json['content'][0]
    }

    def "ユーザを取得する"(id) {
        mvc.perform(get("/api/v1/users/${id}"))
                .andExpect(status().isOk())
                .andReturn().response.getContentAsString()
    }

    def "ユーザを更新する"(id, json) {
        mvc.perform(put("/api/v1/users/${id}")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString('"message":"正常終了"')))
    }

    def "ユーザを削除する"(id) {
        mvc.perform(delete("/api/v1/users/${id}"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString('"message":"正常終了"')))
    }
}
