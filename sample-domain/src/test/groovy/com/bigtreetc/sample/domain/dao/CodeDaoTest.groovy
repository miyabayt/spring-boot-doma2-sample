package com.bigtreetc.sample.domain.dao

import com.bigtreetc.sample.domain.BaseTestContainerSpec
import com.bigtreetc.sample.domain.entity.Code
import com.bigtreetc.sample.domain.entity.CodeCriteria
import com.bigtreetc.sample.domain.exception.NoDataFoundException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.dao.OptimisticLockingFailureException
import org.springframework.data.domain.Pageable

import static com.bigtreetc.sample.domain.util.DomaUtils.createSelectOptions
import static java.util.stream.Collectors.toList
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.NONE

@SpringBootTest(webEnvironment = NONE)
class CodeDaoTest extends BaseTestContainerSpec {

    @Autowired
    CodeDao codeDao

    def "存在しないIDで絞り込んだ場合、空のリストが返ること"() {
        when:
        def options = createSelectOptions(Pageable.unpaged()).count()
        def criteria = new CodeCriteria()
        criteria.setId(-9999)

        def data = codeDao.selectAll(criteria, options, toList())

        then:
        data.size() == 0
    }

    def "存在しない名称で絞り込んだ場合、emptyが返ること"() {
        when:
        def criteria = new CodeCriteria()
        criteria.setCodeName("XXXXXXXXXX")

        Optional<Code> code = codeDao.select(criteria)

        then:
        code == Optional.empty()
    }

    def "存在しないIDで絞り込んだ場合、emptyが返ること"() {
        when:
        Optional<Code> code = codeDao.selectById(-9999)

        then:
        code == Optional.empty()
    }

    def "改定番号を指定しないで更新した場合、例外がスローされること"() {
        when:
        def code = new Code()
        code.setCodeName("XXXXXXXXXX")
        codeDao.update(code)

        then:
        thrown(OptimisticLockingFailureException)
    }

    def "存在するデータを指定して更新した場合、更新件数が1件になること"() {
        when:
        def code = codeDao.selectById(1)
                .orElseThrow({ new NoDataFoundException("データが見つかりません。") })

        code.setCodeName("XXXXXXXXXX")
        def updated = codeDao.update(code)

        then:
        updated == 1
    }
}
