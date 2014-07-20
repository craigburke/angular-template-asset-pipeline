package com.craigburke.angular

import asset.pipeline.AssetCompiler
import grails.spring.BeanBuilder
import grails.util.GrailsWebUtil
import grails.util.Holders
import org.codehaus.groovy.grails.commons.DefaultGrailsApplication
import org.codehaus.groovy.grails.commons.GrailsMetaClassUtils
import org.codehaus.groovy.grails.commons.GrailsTagLibClass
import org.codehaus.groovy.grails.commons.metaclass.MetaClassEnhancer
import org.codehaus.groovy.grails.plugins.web.api.TagLibraryApi
import org.codehaus.groovy.grails.plugins.web.taglib.ApplicationTagLib
import org.codehaus.groovy.grails.plugins.web.taglib.FormTagLib
import org.codehaus.groovy.grails.plugins.web.taglib.FormatTagLib
import org.codehaus.groovy.grails.plugins.web.taglib.ValidationTagLib
import org.codehaus.groovy.grails.support.proxy.DefaultProxyHandler
import org.codehaus.groovy.grails.validation.DefaultConstraintEvaluator
import org.codehaus.groovy.grails.web.pages.GroovyPagesTemplateEngine
import org.codehaus.groovy.grails.web.pages.TagLibraryLookup
import org.codehaus.groovy.grails.web.pages.discovery.GrailsConventionGroovyPageLocator
import org.springframework.context.ApplicationContext
import org.codehaus.groovy.grails.commons.TagLibArtefactHandler
import org.springframework.web.context.request.RequestContextHolder

import static com.craigburke.angular.TemplateProcessorUtil.*

class GspTemplateProcessor {

    GroovyPagesTemplateEngine templateEngine

    GspTemplateProcessor(AssetCompiler precompiler) {
        loadTemplateEngine()
    }

    private def loadTemplateEngine() {
        def previousRequest = RequestContextHolder.requestAttributes

        def request = GrailsWebUtil.bindMockWebRequest()
        def appContext = createApplicationContext(request)

        templateEngine = appContext.getBean('groovyPagesTemplateEngine')

        if (previousRequest) {
            RequestContextHolder.requestAttributes = previousRequest
        }
    }


    private ApplicationContext createApplicationContext(def request) {
        def applicationContext

        def tagLibs = [ValidationTagLib, FormTagLib, FormatTagLib, ApplicationTagLib]
        boolean fieldsPlugin = Holders.pluginManager?.allPlugins?.find { it.name == "fields" }
        if (fieldsPlugin) {
            tagLibs << Class.forName("grails.plugin.formfields.FormFieldsTagLib")
        }

        def bb = new BeanBuilder(request.applicationContext)
        bb.beans {
            grailsApplication(DefaultGrailsApplication)
            gspTagLibraryLookup(TagLibraryLookup) { bean -> bean.autowire = true }
            groovyPagesTemplateEngine(GroovyPagesTemplateEngine, request.servletContext) {
                classLoader = GspTemplateProcessor.classLoader
                tagLibraryLookup = ref('gspTagLibraryLookup')
            }
            instanceTagLibraryApi(TagLibraryApi) { bean -> bean.autowire = true }

            if (fieldsPlugin) {
                groovyPageLocator(GrailsConventionGroovyPageLocator) { bean -> bean.autowire = true }
                formFieldsTemplateService(Class.forName('grails.plugin.formfields.FormFieldsTemplateService')) { bean -> bean.autowire = true }
                constraintsEvaluator(DefaultConstraintEvaluator)
                proxyHandler(DefaultProxyHandler)
                beanPropertyAccessorFactory(Class.forName('grails.plugin.formfields.BeanPropertyAccessorFactory')) {
                    grailsApplication = ref('grailsApplication')
                    constraintsEvaluator = ref('constraintsEvaluator')
                    proxyHandler = ref('proxyHandler')
                }
            }

            tagLibs.each { tagLibClass ->
                "${tagLibClass.name}"(tagLibClass) { bean -> bean.autowire = true }
            }
        }

        applicationContext = bb.createApplicationContext()

        TagLibraryApi instanceTagLibraryApi = applicationContext.getBean('instanceTagLibraryApi')
        TagLibraryLookup tagLookup =  applicationContext.getBean('gspTagLibraryLookup')

        tagLibs.each { tagLibClass -> registerTagLib(tagLibClass, tagLookup, instanceTagLibraryApi) }

        applicationContext
    }

    private def registerTagLib(def tagLibClass, TagLibraryLookup tagLookup, TagLibraryApi instanceTagLibraryApi) {
        GrailsTagLibClass tagLib = Holders.grailsApplication.addArtefact(TagLibArtefactHandler.TYPE, tagLibClass)

        MetaClassEnhancer enhancer = new MetaClassEnhancer()
        enhancer.addApi(instanceTagLibraryApi)
        MetaClass mc = GrailsMetaClassUtils.getMetaClass(tagLib)
        enhancer.enhance(mc)

        tagLookup.registerTagLib(tagLib)
    }

    def process(input, assetFile) {
        File file = assetFile.file
        String templateName = file.name.replace('.tpl.gsp', '.html')

        String moduleName = getModuleName file

        String html = renderGsp input.toString()
        String content = formatHtml html

        getTemplateJs(moduleName, templateName, content)
    }

    private String renderGsp(String html) {
        StringWriter writer = new StringWriter()
        String templateName = "angular-template-${Calendar.getInstance().timeInMillis}"
        templateEngine.createTemplate(html, templateName).make().writeTo(writer)
        writer.toString()
    }

}