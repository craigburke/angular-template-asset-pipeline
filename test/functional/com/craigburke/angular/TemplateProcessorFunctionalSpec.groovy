package com.craigburke.angular

import geb.spock.GebReportingSpec

class TemplateProcessorFunctionalSpec extends GebReportingSpec {

    def "go the test page"() {
        when:
        to AngularTestPage

        then:
        at AngularTestPage

        and:
        pageTitle == "Test1"
    }

    def "load different templates"() {
        when:
        to AngularTestPage

        and:
        templateSelect = template

        then:
        pageTitle == title

        and:
        directiveText == "I'm a directive"

        where:
        template        || title
        'test1.html'    || "Test1"
        'test2.html'    || "Test2"
        'test3.html'    || "Test3"
    }



}