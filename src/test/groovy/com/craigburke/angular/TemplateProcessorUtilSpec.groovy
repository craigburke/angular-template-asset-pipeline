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
        TemplateProcessorUtil.toCamelCase(input, convertUnderscores) == result

        where:
        input                      | convertUnderscores || result
        'foo-bar'                  | true               || 'fooBar'
        'FOO-bar'                  | true               || 'fOOBar'
        'FOO_BAR'                  | true               || 'fOOBAR'
        'why_WOULD-ANyoNE_do-THIS' | true               || 'whyWOULDANyoNEDoTHIS'
        'foo-bar'                  | false              || 'fooBar'
        'FOO-bar'                  | false              || 'fOOBar'
        'FOO_BAR'                  | false              || 'FOO_BAR'
        'why_WOULD-ANyoNE_do-THIS' | false              || 'why_WOULDANyoNE_doTHIS'
    }

    def "covert path: #path to module name"() {
        given:
        AssetFile assetFile = new GenericAssetFile(path: path)

        expect:
        TemplateProcessorUtil.getModuleName(assetFile, '', 'templates', true) == result

        where:
        path                                                         || result
        "my-app/templates/foo.tpl.html"                              || 'myApp'
        "my-APP/templates/foo.tpl.html"                              || 'myAPP'

        "my-app/super-COOL-directives/templates/foo.tpl.html"        || 'myApp.superCOOLDirectives'
        "foo-bar1/foo-bAR2/foo-bAr3/FOO-bar4/templates/foo.tpl.html" || 'fooBar1.fooBAR2.fooBAr3.fOOBar4'
        "foo-bar1/foo-bAR2/foo-bAr3/FOO-bar4/templates/foo.tpl.html" || 'fooBar1.fooBAR2.fooBAr3.fOOBar4'
        "fooBar1/fooBar2/fooBar3/fooBar4/foo.tpl.html"               || 'fooBar1.fooBar2.fooBar3.fooBar4'
        "fooBar1/fooBar2/fooBar3/fooBar4/templates/foo.tpl.html"     || 'fooBar1.fooBar2.fooBar3.fooBar4'
    }
    
    def "module name with moduleBaseName set to #baseName"() {
        given:
        AssetFile assetFile = new GenericAssetFile(path: path)

        expect:
        TemplateProcessorUtil.getModuleName(assetFile, baseName, 'templates', true) == result

        where:
        path                                                            | baseName   || result
        "templates/foo.tpl.html"                                        | 'fooApp'   || 'fooApp'
        "bar/templates/foo.tpl.html"                                    | 'fooApp'   || 'fooApp.bar'
        "templates/foo.tpl.html"                                        | ''         || ''
        "bar/templates/foo.tpl.html"                                    | ''         || 'bar'
        
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