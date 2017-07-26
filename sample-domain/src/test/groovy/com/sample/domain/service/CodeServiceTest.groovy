package com.sample.domain.service

import com.sample.domain.service.code.CodeService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.NONE

@SpringBootTest(webEnvironment = NONE)
class CodeServiceTest extends Specification {

    @Autowired
    CodeService codeService

    def "getCodeの結果がnullではないこと"() {
        when:
        def codeList = codeService.getCode()

        then:
        codeList.size() >= 1
    }
}