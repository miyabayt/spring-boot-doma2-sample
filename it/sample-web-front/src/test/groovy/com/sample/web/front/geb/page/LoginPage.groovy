package com.sample.web.front.geb.page

import geb.Page

class LoginPage extends Page{
    static url = 'http://localhost:18080/admin/login'
    static at = {
        title == 'フロント側ログイン | Sample'
    }
    static content = {
        "ログインする"{user='test@sample.com', password='passw0rd' ->
            def f = $('form')
            f.find('[name="loginId"]').value(user)
            f.find('[name="password"]').value(password)
            f.find('[type="submit"]').click()

            waitFor { title != 'フロント側ログイン | Sample' }
        }
    }

}
