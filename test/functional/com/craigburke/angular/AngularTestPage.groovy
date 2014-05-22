package com.craigburke.angular

import geb.Page

class AngularTestPage extends Page {

    static url = ""

    static at = {
        title ==~ /Angular Test Page/
    }

    static content = {
        pageTitle { $("h1").text() }
        directiveText { $("#directive").text() }
        templateSelect { $("#templateSelect") }

    }


}
