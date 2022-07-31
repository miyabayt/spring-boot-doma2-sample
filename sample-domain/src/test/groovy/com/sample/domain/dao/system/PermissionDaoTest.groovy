package com.sample.domain.dao.system

import com.sample.domain.BaseTestContainerSpec
import com.sample.domain.dto.system.Permission
import com.sample.domain.dto.system.PermissionCriteria
import com.sample.domain.exception.NoDataFoundException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.dao.OptimisticLockingFailureException
import org.springframework.data.domain.Pageable

import static com.sample.domain.util.DomaUtils.createSelectOptions
import static java.util.stream.Collectors.toList
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.NONE

@SpringBootTest(webEnvironment = NONE)
class PermissionDaoTest extends BaseTestContainerSpec {

    @Autowired
    PermissionDao permissionDao

    def "存在しないIDで絞り込んだ場合、空のリストが返ること"() {
        when:
        def options = createSelectOptions(Pageable.unpaged()).count()
        def criteria = new PermissionCriteria()
        criteria.setId(-9999)

        def data = permissionDao.selectAll(criteria, options, toList())

        then:
        data.size() == 0
    }

    def "存在しない権限コードで絞り込んだ場合、emptyが返ること"() {
        when:
        def criteria = new PermissionCriteria()
        criteria.setPermissionCode("XXXXXXXXXX")

        Optional<Permission> permission = permissionDao.select(criteria)

        then:
        permission == Optional.empty()
    }

    def "存在しないIDで絞り込んだ場合、emptyが返ること"() {
        when:
        Optional<Permission> permission = permissionDao.selectById(-9999)

        then:
        permission == Optional.empty()
    }

    def "改定番号を指定しないで更新した場合、例外がスローされること"() {
        when:
        def permission = new Permission()
        permission.setPermissionName("XXXXXXXXXX")
        permissionDao.update(permission)

        then:
        thrown(OptimisticLockingFailureException)
    }

    def "存在するデータを指定して更新した場合、更新件数が1件になること"() {
        when:
        def permission = permissionDao.selectById(1)
                .orElseThrow({ new NoDataFoundException("データが見つかりません。") })

        permission.setPermissionName("XXXXXXXXXX")
        def updated = permissionDao.update(permission)

        then:
        updated == 1
    }
}
