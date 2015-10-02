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

    static String getModuleName(AssetFile file, String nameBase, String templateFolder) {
        String name = getPathParts(file, templateFolder).collect { String pathPart -> toCamelCase pathPart }.join('.')
        (nameBase ?: '') + (nameBase && name ? '.' : '') + name
    }

    static String getTemplateJs(String moduleName, String templateName, String content) {
       String templateJs = """\
            |angular.module('${moduleName}').run(['\$templateCache', function(\$templateCache) {
            |    \$templateCache.put('${templateName}', '${content}');
            |}]);"""

         templateJs.stripMargin()
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