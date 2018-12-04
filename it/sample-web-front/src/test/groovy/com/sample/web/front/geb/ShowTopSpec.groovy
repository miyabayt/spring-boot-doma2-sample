package com.sample.web.front.geb

import com.sample.web.front.geb.page.LoginPage
import com.sample.web.front.geb.page.TopPage
import geb.spock.GebSpec

class ShowTop extends GebSpec{
    def "トップページを表示するシナリオ"(){
        when: "ログインする"
        to LoginPage
        ログインする()
        then:
        at TopPage
    }
}
