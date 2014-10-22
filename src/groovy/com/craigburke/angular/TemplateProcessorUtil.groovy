package com.craigburke.angular

import com.googlecode.htmlcompressor.compressor.HtmlCompressor
import grails.util.Holders

class TemplateProcessorUtil {

    static String formatHtml(String html, boolean compressHtml = true, boolean preserveHtmlComments = false) {
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
	
	static def getRelativePathParts(File file) {
        def pathParts = file.path.tokenize(File.separator)
        int assetRootIndex = pathParts.indexOf('grails-app') + 2
        def relativePathParts = pathParts[assetRootIndex + 1..pathParts.size() - 1] - file.name
	}
	
	static String getTemplateName(File file, String templateFolder, boolean includePath = false) {
		String fileName = file.name.replace('.tpl', '')

		if (includePath) {
			def pathParts = getRelativePathParts(file) - templateFolder

			"/${pathParts.join('/')}/${fileName}" 
		}
		else {
			fileName
		}
	}

    static String getModuleName(File file, String templateFolder = "") {
        def pathParts = getRelativePathParts(file) - templateFolder
        pathParts.collect { toCamelCase it }.join('.')
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
