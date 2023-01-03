package com.bigtreetc.sample.web.front.geb


import com.bigtreetc.sample.web.front.Application
import com.bigtreetc.sample.web.front.geb.base.BaseTestContainerGebSpec
import com.bigtreetc.sample.web.front.geb.base.TestcontainersWebDriver
import com.bigtreetc.sample.web.front.geb.page.LoginPage
import com.bigtreetc.sample.web.front.geb.page.TopPage
import org.spockframework.spring.EnableSharedInjection
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.testcontainers.Testcontainers
import spock.lang.Shared


@EnableSharedInjection
@SpringBootTest(
        classes = Application.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ShowTopSpec extends BaseTestContainerGebSpec {

    @Shared
    @LocalServerPort
    Integer serverPort

    void setupSpec() {
        if (driver instanceof TestcontainersWebDriver) {
            Testcontainers.exposeHostPorts(serverPort)
            driver.startContainer()
            baseUrl = "http://host.testcontainers.internal:$serverPort"
        }
    }

    def "トップページを表示するシナリオ"() {
        when: "ログインする"
        to LoginPage
        ログインする()

        then:
        at TopPage
    }
}
