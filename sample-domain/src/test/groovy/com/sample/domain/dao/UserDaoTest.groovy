package com.sample.domain.dao

import com.sample.domain.dao.users.UserDao
import com.sample.domain.dto.user.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.dao.OptimisticLockingFailureException
import org.springframework.transaction.annotation.Transactional
import spock.lang.Specification

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.NONE

@SpringBootTest(webEnvironment = NONE)
@Transactional
// テスト後にロールバックする
class UserDaoTest extends Specification {

    @Autowired
    UserDao userDao

    def "存在しないメールアドレスで絞り込んだ場合、emptyが返ること"() {
        when:
        def where = new User()
        where.setEmail("aaaa")

        Optional<User> user = userDao.select(where)

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

    def "存在するメールアドレスを指定して更新した場合、更新件数が1件になること"() {
        when:
        def user = userDao.selectById(1)

        def updated = user.map({ u ->
            u.setAddress("test")
            int updated = userDao.update(u)
            return updated
        })

        then:
        updated == Optional.of(1)
    }
}
