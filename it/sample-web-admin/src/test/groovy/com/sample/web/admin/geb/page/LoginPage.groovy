package com.sample.web.admin.geb.page

import geb.Page

class LoginPage extends Page{
    static url = 'http://localhost:18081/admin/login'
    static at = {
        title == '管理側ログイン | Sample'
    }
    static content = {
        "ログインする"{user='test@sample.com', password='passw0rd' ->
            def f = $('form')
            f.find('[name="loginId"]').value(user)
            f.find('[name="password"]').value(password)
            f.find('[type="submit"]').click()

            waitFor { title != '管理側ログイン | Sample' }
        }
    }

}
