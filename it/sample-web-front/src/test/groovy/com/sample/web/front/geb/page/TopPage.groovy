package com.sample.web.front.geb.page

import geb.Page

class TopPage extends Page{
    static url = "http://localhost:18080/admin/"
    static at = {
        title == 'Home | Sample'
    }

    static content = {

    }
}
