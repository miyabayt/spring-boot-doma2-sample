package com.sample.web.admin.controller.html.home

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.sample.domain.dto.user.User
import org.hamcrest.Matchers
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
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
@Transactional // テスト後にロールバックする
class UserRestControllerTest extends Specification {

    @Autowired
    WebApplicationContext wac

    @Shared
    MockMvc mvc

    @Shared
    ObjectMapper omp

    /**
     * APIルートパス
     */
    def String apiRoot = "/api/v1/users"

    /**
     * テストクラス全体の初期化
     */
    def setupSpec() {
        omp = new ObjectMapper()
        omp.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
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
    def "API_TEST: ユーザ取得（複数）"() {
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
    def "API_TEST: ユーザ取得（単数）"() {
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
     * Case: ユーザ編集（追加）
     */
    @WithMockUser()
    def "API_TEST: ユーザ編集（追加）"() {
        setup:
        def user = new User()
        user.firstName = "firstName"
        user.lastName = "lastName"
        user.email = "add@api.users.test.com"
        user.tel = "123456789"
        user.zip = "zip"
        user.address = "address"

        when:
        // JSON型式に変換
        def json = omp.writerWithDefaultPrettyPrinter().writeValueAsString(user)
        println(json)
        // リクエスト送信（追加）
        def resultActions = mvc.perform(MockMvcRequestBuilders.
                post(apiRoot).contentType(MediaType.APPLICATION_JSON).content(json))

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
                .andExpect(jsonPath('$.data[0].email').value("add@api.users.test.com"))
        // コンソール出力
                .andDo(MockMvcResultHandlers.print())
    }

    /**
     * Case: ユーザ編集（変更）
     */
    @WithMockUser()
    def "API_TEST: ユーザ編集（変更）"() {
        setup:
        def userId = 1
        def user = new User()
        user.id = userId
        user.firstName = "firstName2"
        user.lastName = "lastName2"
        user.email = "upd@api.users.test.com"
        user.zip = "zip2"
        user.address = "address2"

        when:
        // JSON型式に変換
        def json = omp.writerWithDefaultPrettyPrinter().writeValueAsString(user)
        println(json)
        // リクエスト送信（追加）
        def resultActions = mvc.perform(MockMvcRequestBuilders.
                put(apiRoot + "/" + userId).contentType(MediaType.APPLICATION_JSON).content(json))

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
                .andExpect(jsonPath('$.data[0].first_name').value("firstName2"))
                .andExpect(jsonPath('$.data[0].last_name').value("lastName2"))
                .andExpect(jsonPath('$.data[0].email').value("upd@api.users.test.com"))
                .andExpect(jsonPath('$.data[0].zip').value("zip2"))
                .andExpect(jsonPath('$.data[0].address').value("address2"))
        // コンソール出力
                .andDo(MockMvcResultHandlers.print())
    }

    /**
     * Case: ユーザ編集（削除）
     */
    @WithMockUser()
    def "API_TEST: ユーザ編集（削除）"() {
        setup:
        def userId = 1

        when:
        // JSON型式に変換
        def resultActions = mvc.perform(MockMvcRequestBuilders.
                delete(apiRoot + "/" + userId).contentType(MediaType.APPLICATION_JSON))

        then:
        resultActions
        // 応答共通チェック
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
        // 応答データ（共通）
                .andExpect(jsonPath('$.message').value("正常終了"))
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
