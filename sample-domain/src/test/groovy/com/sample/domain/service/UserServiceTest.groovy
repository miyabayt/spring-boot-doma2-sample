package com.sample.domain.service

import com.sample.domain.dto.user.User
import com.sample.domain.dto.common.Pageable
import com.sample.domain.service.user.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import spock.lang.Specification

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.NONE

@SpringBootTest(webEnvironment = NONE)
@Transactional
// テスト後にロールバックする
class UserServiceTest extends Specification {

    @Autowired
    UserService userService

    def "存在しないメールアドレスで絞り込んだ場合、0件が返ること"() {
        when:
        def where = new User()
        where.setEmail("aaaa")

        def pages = userService.findAll(where, Pageable.DEFAULT_PAGEABLE)

        then:
        pages.getCount() == 0
    }
}