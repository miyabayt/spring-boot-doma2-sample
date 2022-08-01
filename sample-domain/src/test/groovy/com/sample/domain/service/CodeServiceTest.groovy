package com.sample.domain.service

import com.sample.domain.BaseTestContainerSpec
import org.springframework.data.domain.Pageable
import com.sample.domain.dto.system.CodeCriteria
import com.sample.domain.service.system.CodeService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.NONE

@SpringBootTest(webEnvironment = NONE)
class CodeServiceTest extends BaseTestContainerSpec {

    @Autowired
    CodeService codeService

    def "コード一覧が取得できること"() {
        when:
        def criteria = new CodeCriteria()
        def pages = codeService.findAll(criteria, Pageable.unpaged())

        then:
        pages.totalElements >= 1
    }
}
