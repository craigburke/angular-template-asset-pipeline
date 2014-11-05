package com.craigburke.angular

import asset.pipeline.AssetCompiler
import asset.pipeline.AssetPipelineConfigHolder
import static com.craigburke.angular.TemplateProcessorUtil.*

class HTMLTemplateProcessor {

	HTMLTemplateProcessor(AssetCompiler precompiler) { }

	def process(input, assetFile) {
		def config = AssetPipelineConfigHolder.config.angular

		File file = assetFile.file		
		String templateFolder = config?.templateFolder ?: "templates"

		String moduleName = getModuleName(file, templateFolder)

		boolean includePathInName = config?.hasProperty('includePathInName') ? config.includePathInName : false
		String templateName = getTemplateName(file, templateFolder, includePathInName)
		
		boolean compressHtml = config?.hasProperty('compressHtml') ? config.compressHtml : true
		boolean preserveHtmlComments = config?.hasProperty('preserveHtmlComments') ? config.preserveHtmlComments : false
		String content = formatHtml(input.toString(), compressHtml, preserveHtmlComments)
		
		getTemplateJs(moduleName, templateName, content)
    }

}