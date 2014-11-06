package com.craigburke.angular

import asset.pipeline.AssetFile
import com.googlecode.htmlcompressor.compressor.HtmlCompressor

class TemplateProcessorUtil {
	
	static String formatHtml(String html, boolean compressHtml, boolean preserveHtmlComments) {
		html = html.replace("'", "\\'")
		
		if (compressHtml) {
			HtmlCompressor compressor = new HtmlCompressor()
			
			if (preserveHtmlComments) {
				compressor.removeComments = false
			}
			
			html = compressor.compress html
		}
		else {
			html = html.replace("\n", " \\\n")
		}
		
		html
	}
	
	static def getPathParts(AssetFile file, String templateFolder) {
		file.path.tokenize(File.separator) - templateFolder - file.name
	}


	static String getTemplateName(AssetFile file, String templateFolder, boolean includePath) {
		String fileName = file.name.replace('.tpl', '')
		
		if (includePath) {
			def pathParts = getPathParts(file, templateFolder)
			return "/${pathParts.join(File.separator)}/${fileName}" 
		}
		else {
			return fileName
		}
	}
	
	static String getModuleName(AssetFile file, String templateFolder) {
		getPathParts(file, templateFolder).collect { toCamelCase it }.join('.')
	}

	static String getTemplateJs(String moduleName, String templateName, String content) {
		"""
			angular.module('${moduleName}').run(['\$templateCache', function(\$templateCache) {
				\$templateCache.put('${templateName}', '${content}');
			}]);
		"""
	}
	
	static String toCamelCase(String input) {
		input = input?.toLowerCase()
		
		final String separator = "-"
		
		if (input?.contains('_')) {
			input = input.replace('_', separator)
		}
		
		if (!input || !input.contains(separator)) {
			return input
		}
		
		def result = new StringBuilder()
		input.split(separator).eachWithIndex { part, index ->
			if (index > 0 && part?.length() != 0) {
				result.append(part.substring(0, 1).toUpperCase() + part.substring(1))
			}
			else {
				result.append(part ?: "")
			}
		}
		
		result.toString()
	}
}