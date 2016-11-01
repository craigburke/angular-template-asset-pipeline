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
        Map config = (Map)AssetPipelineConfigHolder.config?.angular ?: [:]

        String templateFolder = config.containsKey('templateFolder') ? config.templateFolder : 'templates'
        String moduleNameBase = config.containsKey('moduleNameBase') ? config.moduleNameBase : ''
        boolean convertUnderscores = config.containsKey('convertUnderscores') ? config.convertUnderscores : true
        String moduleName = getModuleName(assetFile, moduleNameBase, templateFolder, convertUnderscores)

        boolean includePathInName = config.containsKey('includePathInName') ? config.includePathInName : true
        String templateName = getTemplateName(assetFile, templateFolder, includePathInName)

        boolean compressHtml = config.containsKey('compressHtml') ? config.compressHtml : true
        boolean preserveHtmlComments = config.containsKey('preserveHtmlComments') ? config.preserveHtmlComments : false
        String content = formatHtml(input.toString(), compressHtml, preserveHtmlComments)

        getTemplateJs(moduleName, templateName, content)
    }

}