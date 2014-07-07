package com.craigburke.angular

import spock.lang.Specification
import spock.lang.Unroll
import spock.lang.Shared

class TemplateProcessorUnitSpec extends Specification {

    final static String ASSET_PATH = "/src/grails-app/assets"
    @Shared TemplateAssetFile templateAssetFile

    def setup() {
        templateAssetFile = new TemplateAssetFile()
        templateAssetFile.file = new File("${ASSET_PATH}/templates/my-app/index.html")
    }

    @Unroll("covert string: #input to camel case")
    def "convert string to camel case"() {
        expect:
        TemplateProcessor.toCamelCase(input) == result

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
        TemplateProcessor.getModuleName(new File(path), templateRoot, separator) == result

        where:
        path                                                                        | templateRoot | separator  || result
        "${ASSET_PATH}/templates/my-app/foo.html"                                   | 'templates'  |  '.'       || 'myApp'
        "${ASSET_PATH}/templates/my-app/foo.html"                                   | ''           |  '.'       || 'templates.myApp'
        "${ASSET_PATH}/templates/my-app/super-COOL-directives/foo.html"             | 'templates'  |  '.'       || 'myApp.superCoolDirectives'
        "${ASSET_PATH}/templates/foo-bar-1/foo-bAR-2/foo-bAr-3/FOO-bar-4/foo.html"  | 'templates'  |  '.'       || 'fooBar1.fooBar2.fooBar3.fooBar4'
        "${ASSET_PATH}/templates/foo-bar-1/foo-bAR-2/foo-bAr-3/FOO-bar-4/foo.html"  | 'templates'  |  '*'       ||'fooBar1*fooBar2*fooBar3*fooBar4'
        "${ASSET_PATH}/templates/foo-bar-1/foo-bAR-2/foo-bAr-3/FOO-bar-4/foo.html"  | 'templates'  |  '|'       || 'fooBar1|fooBar2|fooBar3|fooBar4'
        "${ASSET_PATH}/templates/foo-bar-1/foo-bAR-2/foo-bAr-3/FOO-bar-4/foo.html"  | 'templates'  |  '|'       || 'fooBar1|fooBar2|fooBar3|fooBar4'


    }

    @Unroll("processing HTML: #input")
    def "process HTML input"() {
        given:
        def processor = new TemplateProcessor()

        expect:
        processor.process(input, templateAssetFile).contains(input)
        processor.process(input, templateAssetFile).contains('$templateCache')

        where:
        input << ["<h1>HEY HOW ARE YOU?</h1>", "<blink>Everybody loves blink tags<blink>"]
    }


}
