package com.sample.web.front.geb.page

import geb.Page

class TopPage extends Page {
    static url = "/"
    static at = {
        title == 'Home | Sample'
    }
    static content = {
    }
}
