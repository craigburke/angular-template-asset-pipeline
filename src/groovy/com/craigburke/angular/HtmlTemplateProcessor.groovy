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
		//get content
		String contentAfterCompress = input.toString()
		//replace script type after compress
		contentAfterCompress = contentAfterCompress.replaceAll("text/ng-template","text/x-jquery-tmpl")
		
		String content = formatHtml(contentAfterCompress, compressHtml, preserveHtmlComments)
		
		//replace script type before compress
		content = content.replaceAll("text/x-jquery-tmpl","text/ng-template")

        getTemplateJs(moduleName, templateName, content)
    }



}
