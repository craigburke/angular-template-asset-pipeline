package com.craigburke.angular

import geb.Page

class DirectiveTestPage extends Page {

    static url = "directives"

    static at = {
        title ==~ /Directives Test/
    }

    static content = {
        pageTitle(wait: true) { $("h1").text() }
        directiveText(wait: true) { $("#directive").text() }
        templateSelect(wait: true) { $("#templateSelect") }

    }


}
