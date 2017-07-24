package com.sample.domain.dao

import org.spockframework.util.Assert
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.NONE

@SpringBootTest(webEnvironment = NONE)
class CodeDaoTest extends Specification {

    @Autowired
    CodeDao codeDao

    def "selectAllの結果がnullではないこと"() {
        expect:
        def codeList = codeDao.selectAll()
        Assert.notNull(codeList)
    }
}
