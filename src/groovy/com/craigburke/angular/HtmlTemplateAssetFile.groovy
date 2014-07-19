package com.craigburke.angular

import asset.pipeline.AbstractAssetFile

class HtmlTemplateAssetFile extends AbstractAssetFile {

    static final String contentType = 'application/javascript'
    static extensions = ['tpl.htm', 'tpl.html']
    static final String compiledExtension = 'js'

    static processors = [HtmlTemplateProcessor]

    String directiveForLine(String line) {
        return null
    }


}
