package com.craigburke.angular

import asset.pipeline.AssetCompiler
import asset.pipeline.AssetPipelineConfigHolder
import asset.pipeline.AssetFile
import static com.craigburke.angular.TemplateProcessorUtil.*

import groovy.transform.CompileStatic

@CompileStatic
class HTMLTemplateProcessor {

    HTMLTemplateProcessor(AssetCompiler precompiler) {}

    def process(String input, AssetFile assetFile) {
        Map config = (Map) AssetPipelineConfigHolder.config?.angular

        String templateFolder = config?.templateFolder ?: "templates"

        String moduleName = getModuleName(assetFile, templateFolder)

        boolean includePathInName = config?.containsKey('includePathInName') ? config.includePathInName : false
        String templateName = getTemplateName(assetFile, templateFolder, includePathInName)

        boolean compressHtml = config?.containsKey('compressHtml') ? config.compressHtml : true
        boolean preserveHtmlComments = config?.containsKey('preserveHtmlComments') ? config.preserveHtmlComments : false
        String content = formatHtml(input.toString(), compressHtml, preserveHtmlComments)

        getTemplateJs(moduleName, templateName, content)
    }

}