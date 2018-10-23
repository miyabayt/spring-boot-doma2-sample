package com.sample.domain.dao.system

import com.sample.domain.dao.system.StaffDao
import com.sample.domain.dto.common.Pageable
import com.sample.domain.dto.system.Staff
import com.sample.domain.dto.system.Staff
import com.sample.domain.dto.system.StaffCriteria
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
@Transactional // テスト後にロールバックする
class StaffDaoTest extends Specification {

    @Autowired
    StaffDao staffDao

    def "存在しないIDで絞り込んだ場合、空のリストが返ること"() {
        when:
        def options = createSelectOptions(Pageable.DEFAULT).count()
        def criteria = new StaffCriteria()
        criteria.setId(-9999)

        def data = staffDao.selectAll(criteria, options, toList())

        then:
        data.size() == 0
    }

    def "存在しないメールアドレスで絞り込んだ場合、emptyが返ること"() {
        when:
        def criteria = new StaffCriteria()
        criteria.setEmail("XXXXXXXXXX")

        Optional<Staff> staff = staffDao.select(criteria)

        then:
        staff == Optional.empty()
    }

    def "存在しないIDで絞り込んだ場合、emptyが返ること"() {
        when:
        Optional<Staff> staff = staffDao.selectById(-9999)

        then:
        staff == Optional.empty()
    }

    def "改定番号を指定しないで更新した場合、例外がスローされること"() {
        when:
        def staff = new Staff()
        staff.setEmail("XXXXXXXXXX")
        staffDao.update(staff)

        then:
        thrown(OptimisticLockingFailureException)
    }

    def "存在するデータを指定して更新した場合、更新件数が1件になること"() {
        when:
        def staff = staffDao.selectById(1)
                .orElseThrow({ new NoDataFoundException("データが見つかりません。") })

        staff.setEmail("XXXXXXXXXX")
        def updated = staffDao.update(staff)

        then:
        updated == 1
    }
}
