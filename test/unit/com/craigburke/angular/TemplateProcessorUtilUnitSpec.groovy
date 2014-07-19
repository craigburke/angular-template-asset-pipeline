package com.craigburke.angular

import spock.lang.Specification
import spock.lang.Unroll
import spock.lang.Shared

class TemplateProcessorUtilUnitSpec extends Specification {

    final static String ASSET_PATH = "/src/grails-app/assets"

    @Unroll("covert string: #input to camel case")
    def "convert string to camel case"() {
        expect:
        TemplateProcessorUtil.toCamelCase(input) == result

        where:
        input                           || result
        'foo-bar'                       || 'fooBar'
        'FOO-bar'                       || 'fooBar'
        'FOO_BAR'                       || 'fooBar'
        'why_WOULD-ANyoNE_do-THIS'      || 'whyWouldAnyoneDoThis'
    }

    @Unroll("covert path: #path to module name")
    def "convert path to module name"() {
        expect:
        TemplateProcessorUtil.getModuleName(new File(path)) == result

        where:
        path                                                                        || result
        "${ASSET_PATH}/templates/my-app/foo.html"                                   || 'myApp'
        "${ASSET_PATH}/templates/my-APP/foo.html"                                   || 'myApp'
        "${ASSET_PATH}/templates/my-app/super-COOL-directives/foo.html"             || 'myApp.superCoolDirectives'
        "${ASSET_PATH}/templates/foo-bar-1/foo-bAR-2/foo-bAr-3/FOO-bar-4/foo.html"  || 'fooBar1.fooBar2.fooBar3.fooBar4'
    }



}
