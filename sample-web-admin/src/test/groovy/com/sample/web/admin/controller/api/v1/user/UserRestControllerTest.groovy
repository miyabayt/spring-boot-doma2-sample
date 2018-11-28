package com.sample.web.admin.controller.html.home

import org.hamcrest.Matchers
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.context.WebApplicationContext
import spock.lang.Shared
import spock.lang.Specification

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup

/**
 * ユーザAPIテストクラス
 */
@SpringBootTest
class UserRestControllerTest extends Specification {

    @Autowired
    WebApplicationContext wac

    @Shared
    MockMvc mvc

    /**
     * APIルートパス
     */
    def String apiRoot = "/api/v1/users"

    /**
     * テストクラス全体の初期化
     */
    def setupSpec() {
        // no-op
    }

    /**
     * テストケースごとの初期化
     */
    def setup() {
        mvc = webAppContextSetup(wac).apply(springSecurity()).build()
    }

    /**
     * Case: ユーザ取得（複数）
     */
    @WithMockUser()
    def "API_TEST_CASE: ユーザ取得（複数）"() {
        when:
        def resultActions = mvc.perform(get(apiRoot).contentType(MediaType.APPLICATION_JSON))
        /* Expected Response Data Sample
            {
              "data": [
                {
                  "id": 1,
                  "first_name": "john",
                  "last_name": "doe",
                  "email": "test@sample.com",
                  "tel": "09011112222",
                  "zip": null,
                  "address": "tokyo, chuo-ku 1-2-3"
                }
              ],
              "message": "正常終了",
              "page": 1,
              "total_pages": 1
            }
         */
        then:
        resultActions
        // 応答共通チェック
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
        // 応答データ（共通）
                .andExpect(jsonPath('$.message').value("正常終了"))
                .andExpect(jsonPath('$.page').value(1))
                .andExpect(jsonPath('$.total_pages').value(1))
        // 応答データ（個別）
                .andExpect(jsonPath('$.data').isArray())
        // 応答データ（詳細）
                .andExpect(jsonPath('$.data[0].id').value(1))
                .andExpect(jsonPath('$.data[0].email').value("test@sample.com"))
        // コンソール出力
                .andDo(MockMvcResultHandlers.print())
    }

    /**
     * Case: ユーザ取得（単数）
     */
    @WithMockUser()
    def "API_TEST_CASE: ユーザ取得（単数）"() {
        setup:
        def userId = 1

        when:
        def resultActions = mvc.perform(get(apiRoot + "/" + userId).contentType(MediaType.APPLICATION_JSON))
        /* Expected Response Data Sample
            {
              "data": [
                {
                  "id": 1,
                  "first_name": "john",
                  "last_name": "doe",
                  "email": "test@sample.com",
                  "tel": "09011112222",
                  "zip": null,
                  "address": "tokyo, chuo-ku 1-2-3"
                }
              ],
              "message": "正常終了"
            }
         */
        then:
        resultActions
        // 応答共通チェック
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
        // 応答データ（共通）
                .andExpect(jsonPath('$.message').value("正常終了"))
        // 応答データ（個別）
                .andExpect(jsonPath('$.data').isArray())
                .andExpect(jsonPath('$.data', Matchers.hasSize(1)))
        // 応答データ（詳細）
                .andExpect(jsonPath('$.data[0].id').value(1))
                .andExpect(jsonPath('$.data[0].email').value("test@sample.com"))
        // コンソール出力
                .andDo(MockMvcResultHandlers.print())
    }

    /**
     * テストケースごとの後始末
     */
    def cleanup() {
        // no-op
    }

    /**
     * テストクラス全体の後始末
     */
    def cleanupSpec() {
        // no-op
    }
}
