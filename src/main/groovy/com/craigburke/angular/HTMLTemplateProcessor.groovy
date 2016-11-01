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
        Map assetConfig = (Map)AssetPipelineConfigHolder.config?.angular ?: [:]
        ProcessorConfig config = new ProcessorConfig(assetConfig)

        String moduleName = getModuleName(assetFile, config)

        String templateName = getTemplateName(assetFile, config.templateFolder, config.includePathInName)
        String content = formatHtml(input.toString(), config.compressHtml, config.preserveHtmlComments)

        getTemplateJs(moduleName, templateName, content)
    }

}