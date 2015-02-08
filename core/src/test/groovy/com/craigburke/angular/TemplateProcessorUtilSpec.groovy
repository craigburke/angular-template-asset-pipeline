package com.craigburke.angular

import asset.pipeline.AssetFile
import asset.pipeline.GenericAssetFile
import spock.lang.Specification
import spock.lang.Unroll
import spock.lang.Shared

@Unroll
class TemplateProcessorUtilUnitSpec extends Specification {
    @Shared
            slash = File.separator

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
        TemplateProcessorUtil.getModuleName(assetFile, 'templates') == result

        where:
        path                                                                                      || result
        "my-app${slash}templates${slash}foo.tpl.html"                                             || 'myApp'
        "my-APP${slash}templates${slash}foo.tpl.html"                                             || 'myApp'
        "my-app${slash}super-COOL-directives${slash}templates${slash}foo.tpl.html"                || 'myApp.superCoolDirectives'
        ['foo-bar1', 'foo-bAR2', 'foo-bAr3', 'FOO-bar4', 'templates', 'foo.tpl.html'].join(slash) || 'fooBar1.fooBar2.fooBar3.fooBar4'
    }

    def "covert path: #path to template name #withPathDescription"() {
        given:
        AssetFile assetFile = new GenericAssetFile(path: path)

        expect:
        TemplateProcessorUtil.getTemplateName(assetFile, 'templates', withPath) == result

        where:
        path                                                                                               | withPath || result
        "my-app${slash}templates${slash}foo.tpl.html"                                                      | false    || 'foo.html'
        "my-app${slash}templates${slash}foo.tpl.html"                                                      | true     || "${slash}my-app${slash}foo.html"
        "my-app${slash}super-cool-directives${slash}templates${slash}directive.tpl.html"                   | false    || "directive.html"
        "my-app${slash}super-cool-directives${slash}directive.html"                                        | true     || "${slash}my-app${slash}super-cool-directives${slash}directive.html"
        "foo-bar1${slash}foo-bar2${slash}foo-bar3${slash}foo-bar4${slash}templates${slash}foobar.tpl.html" | false    || "foobar.html"
        "foo-bar1${slash}foo-bar2${slash}foo-bar3${slash}foo-bar4${slash}foobar.html"                      | true     || "${slash}foo-bar1${slash}foo-bar2${slash}foo-bar3${slash}foo-bar4${slash}foobar.html"

        withPathDescription = withPath ? 'with path' : 'without path'
    }
}