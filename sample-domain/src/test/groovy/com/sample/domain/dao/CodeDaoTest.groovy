package com.sample.domain.dao

import com.sample.domain.dao.system.CodeDao
import com.sample.domain.dto.system.Code
import org.seasar.doma.jdbc.SelectOptions
import org.spockframework.util.Assert
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

import static java.util.stream.Collectors.toList
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.NONE

@SpringBootTest(webEnvironment = NONE)
class CodeDaoTest extends Specification {

    @Autowired
    CodeDao codeDao

    def "selectAllの結果がnullではないこと"() {
        expect:
        def where = new Code()
        def options = SelectOptions.get().offset(0).limit(10)
        def codeList = codeDao.selectAll(where, options, toList())
        Assert.notNull(codeList)
    }
}
