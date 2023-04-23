package com.bigtreetc.sample.domain.service

import com.bigtreetc.sample.domain.BaseTestContainerSpec
import com.bigtreetc.sample.domain.entity.UserCriteria
import com.bigtreetc.sample.domain.service.user.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.Pageable

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.NONE

@SpringBootTest(webEnvironment = NONE)
class UserServiceTest extends BaseTestContainerSpec {

    @Autowired
    UserService userService

    def "存在しないメールアドレスで絞り込んだ場合、0件が返ること"() {
        when:
        def criteria = new UserCriteria()
        criteria.setEmail("aaaa")

        def pages = userService.findAll(criteria, Pageable.unpaged())

        then:
        pages.totalElements == 0
    }

    def "住所をIS NULLで絞り込んだ場合、0件が返ること"() {
        when:
        def criteria = new UserCriteria()
        criteria.setOnlyNullAddress(true)

        def pages = userService.findAll(criteria, Pageable.unpaged())

        then:
        pages.totalElements == 0
    }
}
