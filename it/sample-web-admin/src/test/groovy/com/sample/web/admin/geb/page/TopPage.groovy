package com.sample.web.admin.geb.page

import geb.Page

class TopPage extends Page{
    static url = "http://localhost:18081/admin/"
    static at = {
        title == 'Home | Sample'
    }

    static content = {

    }
}
