package com.craigburke.angular

import spock.lang.Specification
import spock.lang.Unroll
import spock.lang.Shared

class TemplateProcessorUtilUnitSpec extends Specification {

    final static String JAVASCRIPT_PATH = "/src/grails-app/assets/javascripts"

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
        TemplateProcessorUtil.getModuleName(new File(path), "templates") == result

        where:
        path                                                                             || result
        "${JAVASCRIPT_PATH}/my-app/templates/foo.html"                                   || 'myApp'
        "${JAVASCRIPT_PATH}/my-APP/templates/foo.html"                                   || 'myApp'
        "${JAVASCRIPT_PATH}/my-app/super-COOL-directives/templates/foo.html"             || 'myApp.superCoolDirectives'
        "${JAVASCRIPT_PATH}/foo-bar1/foo-bAR2/foo-bAr3/FOO-bar4/templates/foo.html"	     || 'fooBar1.fooBar2.fooBar3.fooBar4'
    }

    @Unroll("covert path: #path to template name")
    def "convert path to module name"() {
        expect:
        TemplateProcessorUtil.getTemplateName(new File(path), "templates") == result
		
		and:
        TemplateProcessorUtil.getTemplateName(new File(path), "templates", true) == resultWithPath

        where:
        path                                                                             		|| result			| resultWithPath	
        "${JAVASCRIPT_PATH}/my-app/templates/foo.tpl.html"                                   	|| 'foo.html'		| '/my-app/foo.html'
        "${JAVASCRIPT_PATH}/my-app/super-cool-directives/templates/directive.tpl.html"         	|| 'directive.html'	| '/my-app/super-cool-directives/directive.html'
        "${JAVASCRIPT_PATH}/foo-bar1/foo-bar2/foo-bar3/foo-bar4/templates/foobar.tpl.html"  	|| 'foobar.html'	| '/foo-bar1/foo-bar2/foo-bar3/foo-bar4/foobar.html'
    }



}
