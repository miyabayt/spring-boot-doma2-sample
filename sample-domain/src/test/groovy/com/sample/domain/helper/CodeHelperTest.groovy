package com.sample.domain.helper

import org.spockframework.util.Assert
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.NONE

@SpringBootTest(webEnvironment = NONE)
class CodeHelperTest extends Specification {

    @Autowired
    CodeHelper codeHelper

    def "コード分類一覧が取得できること"() {
        expect:
        def codeCategoryList= codeHelper.getCodeCategories()
        Assert.notNull(codeCategoryList)
    }

    def "コード分類とコードキーを指定してコード値を取得できること"() {
        expect:
        def code = codeHelper.getCode("GNR0001", "01")
        Assert.that(code.getCodeValue() == "男")
    }
}
