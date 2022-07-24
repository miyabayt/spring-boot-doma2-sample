package com.sample.web.front.geb.page

import geb.Page

class LoginPage extends Page {
    static url = '/login'
    static at = {
        title == 'Please sign in'
    }
    static content = {
        "ログインする" { user = 'test@sample.com', password = 'passw0rd' ->
            def f = $('form')
            f.find('[name="username"]').value(user)
            f.find('[name="password"]').value(password)
            f.find('[type="submit"]').click()

            waitFor { title == 'Home | Sample' }
        }
    }
}
