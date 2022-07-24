package com.sample.domain.service

import com.sample.domain.BaseTestContainerSpec
import com.sample.domain.dto.common.Pageable
import com.sample.domain.dto.user.UserCriteria
import com.sample.domain.service.users.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import spock.lang.Specification

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.NONE

@SpringBootTest(webEnvironment = NONE)
class UserServiceTest extends BaseTestContainerSpec {

    @Autowired
    UserService userService

    def "存在しないメールアドレスで絞り込んだ場合、0件が返ること"() {
        when:
        def criteria = new UserCriteria()
        criteria.setEmail("aaaa")

        def pages = userService.findAll(criteria, Pageable.DEFAULT)

        then:
        pages.getCount() == 0
    }

    def "住所をIS NULLで絞り込んだ場合、0件が返ること"() {
        when:
        def criteria = new UserCriteria()
        criteria.setOnlyNullAddress(true)

        def pages = userService.findAll(criteria, Pageable.DEFAULT)

        then:
        pages.getCount() == 0
    }
}
