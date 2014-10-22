package com.craigburke.angular

import asset.pipeline.AssetCompiler
import static com.craigburke.angular.TemplateProcessorUtil.*
import grails.util.Holders

class HtmlTemplateProcessor {

    HtmlTemplateProcessor(AssetCompiler precompiler) { }

    def process(input, assetFile) {
        def config = Holders.config.grails?.assets?.angular
        File file = assetFile.file
		
		String templateFolder = config?.templateFolder ?: "templates"
		
        String moduleName = getModuleName(file, templateFolder)

		boolean includePathInName = config?.includePathInName
        String templateName = getTemplateName(file, templateFolder, includePathInName)
		
	    boolean compressHtml = config?.compressHtml
		boolean preserveHtmlComments = config?.preserveHtmlComments
		String content = formatHtml(input.toString(), compressHtml, preserveHtmlComments)

        getTemplateJs(moduleName, templateName, content)
    }



}
