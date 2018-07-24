package com.sample.domain.service

import com.sample.domain.dto.common.Pageable
import com.sample.domain.dto.system.Code
import com.sample.domain.service.system.CodeService
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
        def where = new Code()
        def pages = codeService.findAll(where, Pageable.NO_LIMIT)

        then:
        pages.getCount() >= 1
    }
}
