package com.craigburke.angular

import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

class HtmlTemplateProcessorUnitSpec extends Specification {

    final static String ASSET_PATH = "/src/grails-app/assets"
    @Shared HtmlTemplateAssetFile assetFile

    def setup() {
        assetFile = new HtmlTemplateAssetFile()
        assetFile.file = new File("${ASSET_PATH}/templates/my-app/index.tpl.html")
    }

    @Unroll("processing HTML: #input")
    def "process HTML input"() {
        given:
        def processor = new HtmlTemplateProcessor()

        expect:
        processor.process(input, assetFile).contains(input)
        processor.process(input, assetFile).contains('$templateCache')

        where:
        input << ["<h1>HEY HOW ARE YOU?</h1>", "<blink>Everybody loves blink tags<blink>"]
    }

}
