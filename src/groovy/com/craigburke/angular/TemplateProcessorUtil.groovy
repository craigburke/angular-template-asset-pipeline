package com.craigburke.angular

import com.googlecode.htmlcompressor.compressor.HtmlCompressor
import grails.util.Holders

class TemplateProcessorUtil {

    static String formatHtml(String html) {
        def config = Holders.config

        boolean compressHtml = config?.grails?.assets?.angular?.compressHtml ?: true
        boolean preserveHtmlComments = config?.grails?.assets?.angular?.preserveHtmlComments ?: false

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

    static String getModuleName(File file) {
        def config = Holders.config

        String moduleSeparator  = config?.grails?.assets?.angular?.moduleSeparator ?: '.'
        String templateRoot = config?.grails?.assets?.angular?.templateRoot ?: 'templates'

        def pathParts = file.path.tokenize(File.separator)
        int assetRootIndex = pathParts.indexOf('grails-app') + 1
        def relativePathParts = pathParts[assetRootIndex + 2..pathParts.size() - 1] - file.name - templateRoot

        relativePathParts.collect { toCamelCase it }.join(moduleSeparator)
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
