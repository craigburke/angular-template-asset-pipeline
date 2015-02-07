package com.craigburke.angular

import asset.pipeline.AssetFile
import com.googlecode.htmlcompressor.compressor.HtmlCompressor
import java.util.regex.Pattern

import groovy.transform.CompileStatic

@CompileStatic
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

        html = html.replaceAll("(\r)?\n", " \\n")
        html
    }

    static List<String> getTokenizeFilePath(AssetFile file) {
        file.path ? file.path.tokenize(Pattern.quote(File.separator)) : []
    }

    static String getFileNameFromPath(AssetFile file) {
        List<String> allPartsOfFile = getTokenizeFilePath file
        allPartsOfFile ? allPartsOfFile[-1] : ''
    }

    static List<String> getPathParts(AssetFile file, String templateFolder) {
        List<String> allPartsOfFile = getTokenizeFilePath file
        String actualFileName = allPartsOfFile ? allPartsOfFile[-1] : ''

        allPartsOfFile - templateFolder - actualFileName
    }


    static String getTemplateName(AssetFile file, String templateFolder, boolean includePath) {
        String fileName = getFileNameFromPath(file).replace('.tpl', '')

        if (includePath) {
            def pathParts = getPathParts(file, templateFolder)
            return "$File.separator${pathParts.join(File.separator)}$File.separator${fileName}"
        } else {
            return fileName
        }
    }

    static String getModuleName(AssetFile file, String templateFolder) {
        getPathParts(file, templateFolder).collect { String pathPart -> toCamelCase pathPart }.join('.')
    }

    static String getTemplateJs(String moduleName, String templateName, String content) {
        """\
			angular.module('${moduleName}').run(['\$templateCache', function(\$templateCache) {
				\$templateCache.put('${templateName}', '${content}');
			}]);
		""".stripIndent()
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
        input.split(Pattern.quote(separator)).eachWithIndex { String part, int index ->
            if (index > 0 && part?.length() != 0) {
                result.append(part.substring(0, 1).toUpperCase() + part.substring(1))
            } else {
                result.append(part ?: "")
            }
        }

        result.toString()
    }
}