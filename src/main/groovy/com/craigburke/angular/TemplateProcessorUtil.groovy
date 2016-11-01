package com.craigburke.angular

import asset.pipeline.AssetFile
import com.googlecode.htmlcompressor.compressor.HtmlCompressor
import java.util.regex.Pattern

import groovy.transform.CompileStatic

@CompileStatic
class TemplateProcessorUtil {

    static String formatHtml(String html, ProcessorConfig config) {

        if (config.compressHtml) {
            HtmlCompressor compressor = new HtmlCompressor(
                removeComments: !config.preserveHtmlComments,
                preserveLineBreaks: config.preserveLineBreaks
            )
            html = compressor.compress(html)
            if (config.preserveLineBreaks) {
                html = proccessLineBreaks(html, true)
            }
        }
        else {
            html = proccessLineBreaks(html, config.preserveLineBreaks)
        }

        html = html.replace("'", "\\'")
        html
    }

    private static String proccessLineBreaks(String input, boolean preserve) {
        String lineBreakReplacement = preserve ? " \\\\n" : ' '
        input.replaceAll("(\r)?\n", lineBreakReplacement)
    }

    static String getFileNameFromPath(AssetFile file) {
        List<String> pathParts = file.path.tokenize('/')
        pathParts ? pathParts[-1] : ''
    }

    static List<String> getPathParts(AssetFile file, String templateFolder) {
        List<String> allPathParts = file.path.tokenize('/')
        allPathParts - templateFolder - getFileNameFromPath(file)
    }

    static String getTemplateName(AssetFile file, String templateFolder, boolean includePath) {
        String fileName = getFileNameFromPath(file).replace('.tpl', '')

        if (includePath) {
            def pathParts = getPathParts(file, templateFolder)
            "/${pathParts? (pathParts.join('/') + '/') : ''}${fileName}".toString()
        } else {
            fileName
        }
    }

    static String getModuleName(AssetFile file, ProcessorConfig config) {
        if (config.templateModuleName) {
            config.templateModuleName
        }
        else {
            String name = getPathParts(file, config.templateFolder).collect { String pathPart -> toCamelCase(pathPart, config.convertUnderscores) }.join('.')
            (config.moduleBaseName ?: '') + (config.moduleBaseName && name ? '.' : '') + name
        }
    }

    static String getTemplateJs(String moduleName, String templateName, String content) {
       String templateJs = """\
            |angular.module('${moduleName}').run(['\$templateCache', function(\$templateCache) {
            |    \$templateCache.put('${templateName}', '${content}');
            |}]);"""

         templateJs.stripMargin()
    }

    static String toCamelCase(String input, boolean convertUnderscores) {
        final String separator = "-"

        if (convertUnderscores && input?.contains('_')) {
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
                result.append(part.substring(0, 1).toLowerCase() + part.substring(1) ?: "")
            }
        }

        result.toString()
    }
}
