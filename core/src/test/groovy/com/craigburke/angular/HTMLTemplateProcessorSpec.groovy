package com.craigburke.angular

import asset.pipeline.AssetFile
import asset.pipeline.GenericAssetFile

import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

class HtmlTemplateProcessorUnitSpec extends Specification {

    @Shared AssetFile assetFile

    def setup() {
        assetFile = new GenericAssetFile(path: 'foo/bar')
    }

    @Unroll("processing HTML: #input")
    def "process HTML input"() {
        given:
        def processor = new HTMLTemplateProcessor()

        expect:
        processor.process(input, assetFile).contains(input)
        processor.process(input, assetFile).contains('$templateCache')

        where:
        input << ["<h1>HEY HOW ARE YOU?</h1>", "<blink>Everybody loves blink tags<blink>"]
    }

}