package com.craigburke.angular

import asset.pipeline.AssetCompiler
import grails.spring.BeanBuilder
import grails.util.GrailsWebUtil
import grails.util.Holders
import org.codehaus.groovy.grails.commons.DefaultGrailsApplication
import org.codehaus.groovy.grails.commons.GrailsTagLibClass
import org.codehaus.groovy.grails.commons.metaclass.MetaClassEnhancer
import org.codehaus.groovy.grails.plugins.DefaultGrailsPluginManager
import org.codehaus.groovy.grails.plugins.codecs.StandaloneCodecLookup
import org.codehaus.groovy.grails.plugins.web.api.TagLibraryApi
import org.codehaus.groovy.grails.plugins.web.taglib.FormTagLib
import org.codehaus.groovy.grails.plugins.web.taglib.FormatTagLib
import org.codehaus.groovy.grails.plugins.web.taglib.RenderTagLib
import org.codehaus.groovy.grails.plugins.web.taglib.ValidationTagLib
import org.codehaus.groovy.grails.support.proxy.DefaultProxyHandler
import org.codehaus.groovy.grails.validation.DefaultConstraintEvaluator
import org.codehaus.groovy.grails.web.pages.GroovyPageResourceLoader
import org.codehaus.groovy.grails.web.pages.GroovyPagesTemplateEngine
import org.codehaus.groovy.grails.web.pages.GroovyPagesTemplateRenderer
import org.codehaus.groovy.grails.web.pages.TagLibraryLookup
import org.codehaus.groovy.grails.web.pages.discovery.GrailsConventionGroovyPageLocator
import org.codehaus.groovy.grails.web.servlet.mvc.GrailsWebRequest
import org.springframework.context.ApplicationContext
import org.codehaus.groovy.grails.commons.TagLibArtefactHandler
import org.springframework.core.io.FileSystemResource

import static com.craigburke.angular.TemplateProcessorUtil.*

class GspTemplateProcessor {

    GroovyPagesTemplateEngine templateEngine

    GspTemplateProcessor(AssetCompiler precompiler) {
        loadTemplateEngine()
    }

    private def loadTemplateEngine() {
        def appContext = Holders.grailsApplication?.mainContext

        if (!appContext?.containsBean('groovyPagesTemplateEngine')) {
            def request = GrailsWebUtil.bindMockWebRequest()
            appContext = createApplicationContext(request)
        }

        templateEngine = appContext.groovyPagesTemplateEngine
    }

    private getApplicationRoot(GrailsWebRequest request) {
        String currentPath = request.servletContext.getRealPath('/')
        def possibleLocations = ['target', 'build/resources']

        String location = possibleLocations.find { currentPath.contains it }
        currentPath?.split(location).first()
    }


    private ApplicationContext createApplicationContext(GrailsWebRequest request) {
        def appContext
        def classLoader = GspTemplateProcessor.classLoader

        def tagLibs = [ValidationTagLib, FormTagLib, FormatTagLib, RenderTagLib]
        boolean fieldsPluginInstalled = Holders.pluginManager?.allPlugins?.find { it.name == 'fields' }
        if (fieldsPluginInstalled) {
            tagLibs << classLoader.loadClass('grails.plugin.formfields.FormFieldsTagLib')
        }

        String appRoot = getApplicationRoot(request)

        def beanBuilder = new BeanBuilder()
        beanBuilder.beans {

            grailsApplication(DefaultGrailsApplication) { bean -> bean.autowire = true }
            gspTagLibraryLookup(TagLibraryLookup) { bean -> bean.autowire = true }

            groovyPagesTemplateEngine(GroovyPagesTemplateEngine, request.servletContext) {
                classLoader = classLoader
                tagLibraryLookup = gspTagLibraryLookup
            }
            instanceTagLibraryApi(TagLibraryApi) { bean -> bean.autowire = true }

            if (fieldsPluginInstalled) {
                pluginManager(DefaultGrailsPluginManager, [] as String[], grailsApplication) { bean -> bean.autowire = true }
                groovyPageResourceLoader(GroovyPageResourceLoader) {
                    baseResource = new FileSystemResource(appRoot)
                }
                groovyPagesTemplateRenderer(GroovyPagesTemplateRenderer) { bean -> bean.autowire = true }
                codecLookup(StandaloneCodecLookup)
                groovyPageLocator(GrailsConventionGroovyPageLocator) { bean -> bean.autowire = true }
                formFieldsTemplateService(classLoader.loadClass('grails.plugin.formfields.FormFieldsTemplateService')) { bean -> bean.autowire = true }
                constraintsEvaluator(DefaultConstraintEvaluator)
                proxyHandler(DefaultProxyHandler)
                beanPropertyAccessorFactory(classLoader.loadClass('grails.plugin.formfields.BeanPropertyAccessorFactory')) { bean -> bean.autowire = true }
            }

            tagLibs.each { tagLibClass ->
                "${tagLibClass.name}"(tagLibClass) { bean ->  bean.autowire = true }
            }
        }

        appContext = beanBuilder.createApplicationContext()

        if (fieldsPluginInstalled) {
            appContext.groovyPageLocator.addResourceLoader(appContext.groovyPageResourceLoader)
        }

        tagLibs.each { tagLibClass ->
            registerTagLib(appContext."${tagLibClass.name}", appContext.gspTagLibraryLookup, appContext.instanceTagLibraryApi)
        }

        appContext
    }

    private def registerTagLib(def bean, def tagLookup, def instanceTagLibraryApi) {
        GrailsTagLibClass tagLib = Holders.grailsApplication.addArtefact(TagLibArtefactHandler.TYPE, bean.class)

        MetaClassEnhancer enhancer = new MetaClassEnhancer()
        enhancer.addApi(instanceTagLibraryApi)
        enhancer.enhance(bean.metaClass)

        tagLookup.registerTagLib(tagLib)
    }

    def process(input, assetFile) {
        File file = assetFile.file
        String templateName = file.name.replace('.tpl.gsp', '.html')

        String moduleName = getModuleName file

        String html = renderGsp(file, input.toString())
        String content = formatHtml html

        getTemplateJs(moduleName, templateName, content)
    }

    private String renderGsp(File file, String html) {
        StringWriter writer = new StringWriter()
        String templateName = "${file.path}-${Calendar.instance.timeInMillis}"

        templateEngine.createTemplate(html, templateName).make().writeTo(writer)
        writer.toString()
    }
}