package com.craigburke.angular

import geb.Page

class FieldsTestPage extends Page {

    static url = "fields"

    static at = {
        title ==~ /Fields Test/
    }

    static content = {
        pageTitle(wait: true) { $("h1").text() }
        name {$("#name")}
        number {$("#number")}
        numberValue { $("#number-value").text() }
        nameValue { $("#name-value").text() }
    }


}
