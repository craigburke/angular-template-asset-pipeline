package com.craigburke.angular

import asset.pipeline.AssetFile
import asset.pipeline.GenericAssetFile
import spock.lang.Specification
import spock.lang.Unroll
import spock.lang.Shared

@Unroll
class TemplateProcessorUtilUnitSpec extends Specification {

    def "covert string: #input to camel case"() {
        expect:
        TemplateProcessorUtil.toCamelCase(input) == result

        where:
        input                      || result
        'foo-bar'                  || 'fooBar'
        'FOO-bar'                  || 'fooBar'
        'FOO_BAR'                  || 'fooBar'
        'why_WOULD-ANyoNE_do-THIS' || 'whyWouldAnyoneDoThis'
    }

    def "covert path: #path to module name"() {
        given:
        AssetFile assetFile = new GenericAssetFile(path: path)

        expect:
        TemplateProcessorUtil.getModuleName(assetFile, '', 'templates', includeSectionInModuleName) == result

        where:
        path                                                         | includeSectionInModuleName || result
        "my-app/templates/foo.tpl.html"                              | true                       || 'myApp'
        "my-APP/templates/foo.tpl.html"                              | true                       || 'myApp'
        "my-app/super-COOL-directives/templates/foo.tpl.html"        | true                       || 'myApp.superCoolDirectives'
        "foo-bar1/foo-bAR2/foo-bAr3/FOO-bar4/templates/foo.tpl.html" | true                       || 'fooBar1.fooBar2.fooBar3.fooBar4'
        "my-app/templates/foo.tpl.html"                              | false                      || ''
        "my-APP/templates/foo.tpl.html"                              | false                      || ''
        "my-app/super-COOL-directives/templates/foo.tpl.html"        | false                      || ''
        "foo-bar1/foo-bAR2/foo-bAr3/FOO-bar4/templates/foo.tpl.html" | false                      || ''
    }

    def "module name with moduleBaseName set to #baseName"() {
        given:
        AssetFile assetFile = new GenericAssetFile(path: path)

        expect:
        TemplateProcessorUtil.getModuleName(assetFile, baseName, 'templates', includeSectionInModuleName) == result

        where:
        path                         | baseName | includeSectionInModuleName || result
        "templates/foo.tpl.html"     | 'fooApp' | true                       || 'fooApp'
        "bar/templates/foo.tpl.html" | 'fooApp' | true                       || 'fooApp.bar'
        "templates/foo.tpl.html"     | ''       | true                       || ''
        "bar/templates/foo.tpl.html" | ''       | true                       || 'bar'
        "templates/foo.tpl.html"     | 'fooApp' | false                      || 'fooApp'
        "bar/templates/foo.tpl.html" | 'fooApp' | false                      || 'fooApp'
        "templates/foo.tpl.html"     | ''       | false                      || ''
        "bar/templates/foo.tpl.html" | ''       | false                      || ''

    }

    def "covert path: #path to template name #withPathDescription"() {
        given:
        AssetFile assetFile = new GenericAssetFile(path: path)

        expect:
        TemplateProcessorUtil.getTemplateName(assetFile, 'templates', withPath) == result

        where:
        path                                                            | withPath || result
        "my-app/templates/foo.tpl.html"                                 | false    || 'foo.html'
        "my-app/templates/foo.tpl.html"                                 | true     || "/my-app/foo.html"
        "my-app/super-cool-directives/templates/directive.tpl.html"     | false    || "directive.html"
        "my-app/super-cool-directives/directive.html"                   | true     || "/my-app/super-cool-directives/directive.html"
        "foo-bar1/foo-bar2/foo-bar3/foo-bar4/templates/foobar.tpl.html" | false    || "foobar.html"
        "foo-bar1/foo-bar2/foo-bar3/foo-bar4/foobar.html"               | true     || "/foo-bar1/foo-bar2/foo-bar3/foo-bar4/foobar.html"

        withPathDescription = withPath ? 'with path' : 'without path'
    }
}