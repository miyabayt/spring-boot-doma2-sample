package com.bigtreetc.sample.domain.service

import com.bigtreetc.sample.domain.BaseTestContainerSpec
import com.bigtreetc.sample.domain.entity.CodeCriteria
import com.bigtreetc.sample.domain.service.code.CodeService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.Pageable

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
