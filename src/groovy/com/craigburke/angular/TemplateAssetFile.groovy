package com.craigburke.angular

import asset.pipeline.AbstractAssetFile

class TemplateAssetFile extends AbstractAssetFile {

    static final String contentType = 'application/javascript'
    static extensions = ['tpl.htm', 'tpl.html', 'tpl.gsp']
    static final String compiledExtension = 'js'

    static processors = [TemplateProcessor]

    String directiveForLine(String line) {
        return null
    }


}
