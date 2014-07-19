package com.craigburke.angular

import grails.test.mixin.TestMixin
import grails.test.mixin.support.GrailsUnitTestMixin
import grails.util.Holders
import org.springframework.transaction.UnexpectedRollbackException
import spock.lang.Shared
import spock.lang.Specification

@TestMixin(GrailsUnitTestMixin)
class GspTemplateProcessorUnitSpec extends Specification {

    final static String ASSET_PATH = "/src/grails-app/assets"
    @Shared HtmlTemplateAssetFile assetFile

    def setup() {
        assetFile = new HtmlTemplateAssetFile()
        assetFile.file = new File("${ASSET_PATH}/templates/my-app/index.tpl.gsp")

        Holders.pluginManager.metaClass.allPlugins = [
            [name: 'fields']
        ]
    }

    def "fields integration test"() {
        given:
        def processor = new GspTemplateProcessor()

        expect:
        String result = processor.process(input, assetFile)

        !result.contains(tag)
        result.contains(output)
        result.contains('$templateCache')

        where:
        tag = 'f:field'
        input = '<f:with bean="${new com.craigburke.angular.Foo()}" ><f:field property="name" /></f:with>'
        output = '<input'
    }
}
