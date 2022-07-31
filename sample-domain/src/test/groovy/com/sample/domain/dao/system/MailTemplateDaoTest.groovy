package com.sample.domain.dao.system

import com.sample.domain.BaseTestContainerSpec
import com.sample.domain.dto.common.Pageable
import com.sample.domain.dto.system.MailTemplate
import com.sample.domain.dto.system.MailTemplateCriteria
import com.sample.domain.exception.NoDataFoundException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.dao.OptimisticLockingFailureException
import org.springframework.transaction.annotation.Transactional
import spock.lang.Specification

import static com.sample.domain.util.DomaUtils.createSelectOptions
import static java.util.stream.Collectors.toList
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.NONE

@SpringBootTest(webEnvironment = NONE)
class MailTemplateDaoTest extends BaseTestContainerSpec {

    @Autowired
    MailTemplateDao mailTemplateDao

    def "存在しないIDで絞り込んだ場合、空のリストが返ること"() {
        when:
        def options = createSelectOptions(Pageable.DEFAULT).count()
        def criteria = new MailTemplateCriteria()
        criteria.setId(-9999)

        def data = mailTemplateDao.selectAll(criteria, options, toList())

        then:
        data.size() == 0
    }

    def "存在しない名称で絞り込んだ場合、emptyが返ること"() {
        when:
        def criteria = new MailTemplateCriteria()
        criteria.setTemplateCode("XXXXXXXXXX")

        Optional<MailTemplate> mailTemplate = mailTemplateDao.select(criteria)

        then:
        mailTemplate == Optional.empty()
    }

    def "存在しないIDで絞り込んだ場合、emptyが返ること"() {
        when:
        Optional<MailTemplate> mailTemplate = mailTemplateDao.selectById(-9999)

        then:
        mailTemplate == Optional.empty()
    }

    def "改定番号を指定しないで更新した場合、例外がスローされること"() {
        when:
        def mailTemplate = new MailTemplate()
        mailTemplate.setSubject("XXXXXXXXXX")
        mailTemplateDao.update(mailTemplate)

        then:
        thrown(OptimisticLockingFailureException)
    }

    def "存在するデータを指定して更新した場合、更新件数が1件になること"() {
        when:
        def mailTemplate = mailTemplateDao.selectById(1)
                .orElseThrow({ new NoDataFoundException("データが見つかりません。") })

        mailTemplate.setSubject("XXXXXXXXXX")
        def updated = mailTemplateDao.update(mailTemplate)

        then:
        updated == 1
    }
}
