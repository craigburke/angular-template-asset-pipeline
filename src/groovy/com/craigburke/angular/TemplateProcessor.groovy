package com.craigburke.angular

import asset.pipeline.AbstractProcessor
import asset.pipeline.AssetCompiler
import grails.util.Holders
import com.googlecode.htmlcompressor.compressor.HtmlCompressor


class TemplateProcessor extends AbstractProcessor {

    TemplateProcessor(AssetCompiler precompiler) {
        super(precompiler)
    }

    def process(input, assetFile) {
        File file = assetFile.file

        def config = Holders.grailsApplication?.config

        String moduleSeparator  = config?.grails?.assets?.angular?.moduleSeparator ?: '.'
        boolean compressHtml = config?.grails?.assets?.angular?.compressHtml ?: true

        String moduleName = getModuleName(file.absolutePath, moduleSeparator)
        String templateName = file.name.replace('.tpl', '')
        String content = formatHtml(input, compressHtml)

        return """
            angular.module('${moduleName}').run(['\$templateCache', function(\$templateCache) {
                \$templateCache.put('${templateName}', '${content}');
            }]);
        """
    }

    static String formatHtml(html, boolean compressHtml) {
        html = html.replace("'", "\\'")

        if (compressHtml) {
            HtmlCompressor compressor = new HtmlCompressor()
            html = compressor.compress(html)
        }
        else {
            html = html.replace("\n", " \\\n")
        }

        html
    }

    static String getModuleName(String filePath, String moduleSeparator) {
        String assetRoot = filePath.split('/grails-app/assets/').last()
        def pathParts = assetRoot.tokenize('/')

        String rootFolder = pathParts.size() == 1 ? "" : pathParts.first()
        String fileName = pathParts.last()
        String relativePath = (assetRoot - rootFolder - fileName)

        def moduleParts = relativePath.tokenize('/').collect { toCamelCase it }

        moduleParts.join(moduleSeparator)
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
