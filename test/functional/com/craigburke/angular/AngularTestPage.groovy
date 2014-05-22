package com.craigburke.angular

import geb.Page

class AngularTestPage extends Page {

    static url = ""

    static at = {
        title ==~ /Angular Test Page/
    }

    static content = {
        pageTitle(wait: true) { $("h1").text() }
        directiveText(wait: true) { $("#directive").text() }
        templateSelect(wait: true) { $("#templateSelect") }

    }


}
