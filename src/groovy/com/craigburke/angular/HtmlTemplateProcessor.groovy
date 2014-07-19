package com.craigburke.angular

import asset.pipeline.AssetCompiler
import static com.craigburke.angular.TemplateProcessorUtil.*

class HtmlTemplateProcessor {

    HtmlTemplateProcessor(AssetCompiler precompiler) { }

    def process(input, assetFile) {
        File file = assetFile.file

        String moduleName = getModuleName file
        String templateName = file.name.replace('.tpl', '')
        String content = formatHtml input.toString()

        getTemplateJs(moduleName, templateName, content)
    }



}
