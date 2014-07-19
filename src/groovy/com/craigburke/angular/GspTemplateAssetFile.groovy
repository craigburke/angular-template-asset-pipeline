package com.craigburke.angular

import asset.pipeline.AbstractAssetFile

class GspTemplateAssetFile extends AbstractAssetFile {

    static final String contentType = 'application/javascript'
    static extensions = ['tpl.gsp']
    static final String compiledExtension = 'js'

    static processors = [GspTemplateProcessor]

    String directiveForLine(String line) {
        return null
    }


}
