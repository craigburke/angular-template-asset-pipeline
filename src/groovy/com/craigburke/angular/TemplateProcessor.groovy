package com.craigburke.angular

import asset.pipeline.AssetCompiler
import com.googlecode.htmlcompressor.compressor.HtmlCompressor
import org.codehaus.groovy.grails.web.pages.GroovyPagesTemplateEngine
import grails.util.Holders
import java.security.MessageDigest

class TemplateProcessor {

    GroovyPagesTemplateEngine templateEngine

    TemplateProcessor(AssetCompiler precompiler) {
        templateEngine = Holders.grailsApplication?.mainContext?.getBean('groovyPagesTemplateEngine')
    }

    def process(input, assetFile) {
        File file = assetFile.file

        def config = Holders.grailsApplication?.config

        String moduleSeparator  = config?.grails?.assets?.angular?.moduleSeparator ?: '.'
        String templateRoot = config?.grails?.assets?.angular?.templateRoot ?: 'templates'
        boolean compressHtml = config?.grails?.assets?.angular?.compressHtml ?: true
        boolean preserveHtmlComments = config?.grails?.assets?.angular?.preserveHtmlComments ?: false

        String templateName = file.name.replace('.tpl', '').replace('.gsp', '.html')

        String html = file.name.endsWith('.gsp') ? renderGsp(input.toString()) : input.toString()

        String moduleName = getModuleName(file, templateRoot, moduleSeparator)
        String content = formatHtml(html, compressHtml, preserveHtmlComments)

        return """
            angular.module('${moduleName}').run(['\$templateCache', function(\$templateCache) {
                \$templateCache.put('${templateName}', '${content}');
            }]);
        """
    }

    private String renderGsp(String html) {
        StringWriter writer = new StringWriter()

        Formatter hexHash = new Formatter()
        MessageDigest.getInstance("SHA-1").digest(html.bytes).each {
            b -> hexHash.format('%02x', b)
        }

        String templateName = "angular-template-${hexHash}"
        templateEngine.createTemplate(html, templateName).make().writeTo(writer)
        writer.toString()
    }

    static String formatHtml(String html, boolean compressHtml, boolean preserveHtmlComments) {
        html = html.replace("'", "\\'")

        if (compressHtml) {
            HtmlCompressor compressor = new HtmlCompressor()

            if (preserveHtmlComments) {
                compressor.setRemoveComments(false)
            }

            html = compressor.compress(html)
        }
        else {
            html = html.replace("\n", " \\\n")
        }

        html
    }

    static String getModuleName(File file, String templateRoot, String moduleSeparator) {
        def pathParts = file.path.tokenize(File.separator)
        int assetRootIndex = pathParts.indexOf('grails-app') + 1
        def relativePathParts = pathParts[assetRootIndex + 1..pathParts.size() - 1] - file.name - templateRoot

        relativePathParts.collect { toCamelCase it }.join(moduleSeparator)
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
