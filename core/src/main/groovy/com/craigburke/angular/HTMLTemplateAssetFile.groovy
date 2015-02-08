package com.craigburke.angular

import asset.pipeline.AbstractAssetFile
import com.craigburke.angular.HTMLTemplateProcessor

class HTMLTemplateAssetFile extends AbstractAssetFile {
    static final String contentType = 'application/javascript'
    static extensions = ['tpl.htm', 'tpl.html']
    static final String compiledExtension = 'js'

    static processors = [HTMLTemplateProcessor]

    static String directiveForLine(String line) {
        return null
    }
}