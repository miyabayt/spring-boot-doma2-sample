package com.bigtreetc.sample.domain.dao

import com.bigtreetc.sample.domain.BaseTestContainerSpec
import com.bigtreetc.sample.domain.entity.User
import com.bigtreetc.sample.domain.entity.UserCriteria
import com.bigtreetc.sample.domain.exception.NoDataFoundException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.dao.OptimisticLockingFailureException
import org.springframework.data.domain.Pageable

import static com.bigtreetc.sample.domain.util.DomaUtils.createSelectOptions
import static java.util.stream.Collectors.toList
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.NONE

@SpringBootTest(webEnvironment = NONE)
class UserDaoTest extends BaseTestContainerSpec {

    @Autowired
    UserDao userDao

    def "存在しないIDで絞り込んだ場合、空のリストが返ること"() {
        when:
        def options = createSelectOptions(Pageable.unpaged()).count()
        def criteria = new UserCriteria()
        criteria.setId(-9999)

        def data = userDao.selectAll(criteria, options, toList())

        then:
        data.size() == 0
    }

    def "存在しないメールアドレスで絞り込んだ場合、emptyが返ること"() {
        when:
        def criteria = new UserCriteria()
        criteria.setEmail("aaaa")

        Optional<User> user = userDao.select(criteria)

        then:
        user == Optional.empty()
    }

    def "存在しないIDで絞り込んだ場合、emptyが返ること"() {
        when:
        Optional<User> user = userDao.selectById(-9999)

        then:
        user == Optional.empty()
    }

    def "改定番号を指定しないで更新した場合、例外がスローされること"() {
        when:
        def user = new User()
        user.setEmail("test@sample.com")
        userDao.update(user)

        then:
        thrown(OptimisticLockingFailureException)
    }

    def "存在するデータを指定して更新した場合、更新件数が1件になること"() {
        when:
        def user = userDao.selectById(1)
                .orElseThrow({ new NoDataFoundException("データが見つかりません。") })

        user.setAddress("test")
        def updated = userDao.update(user)

        then:
        updated == 1
    }
}
