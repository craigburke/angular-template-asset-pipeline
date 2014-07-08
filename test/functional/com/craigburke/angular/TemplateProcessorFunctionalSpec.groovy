package com.craigburke.angular

import geb.spock.GebReportingSpec
import spock.lang.Unroll

class TemplateProcessorFunctionalSpec extends GebReportingSpec {

    def "go the test page"() {
        when:
        to AngularTestPage

        then:
        at AngularTestPage

        and:
        pageTitle == "Test1"
    }

    @Unroll("load template: #template")
    def "load different templates"() {
        when:
        to AngularTestPage

        and:
        templateSelect = template

        then:
        waitFor {
            pageTitle == title
            directiveText == "I'm a directive!"
        }

        and:

        where:
        template        || title
        'test1.html'    || "Test1"
        'test2.html'    || "Test2"
        'test3.html'    || "Test3"
        'test4.html'    || "Test4"
    }


}