package com.bigtreetc.sample.domain.dao

import com.bigtreetc.sample.domain.BaseTestContainerSpec
import com.bigtreetc.sample.domain.entity.CodeCriteria
import org.seasar.doma.jdbc.SelectOptions
import org.spockframework.util.Assert
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

import static java.util.stream.Collectors.toList
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.NONE

@SpringBootTest(webEnvironment = NONE)
class CodeDaoTest extends BaseTestContainerSpec {

    @Autowired
    CodeDao codeDao

    def "selectAllの結果がnullではないこと"() {
        expect:
        def criteria = new CodeCriteria()
        def options = SelectOptions.get().offset(0).limit(10)
        def codeList = codeDao.selectAll(criteria, options, toList())
        Assert.notNull(codeList)
    }
}
