package com.craigburke.angular

import groovy.transform.CompileStatic

@CompileStatic
class ProcessorConfig {

    final String templateModuleName
    final String templateFolder
    final String moduleBaseName
    final Boolean convertUnderscores
    final Boolean includePathInName

    final Boolean compressHtml
    final Boolean preserveHtmlComments
    final Boolean preserveLineBreaks

    ProcessorConfig(Map config = [:]) {
        templateModuleName = config.containsKey('templateModuleName') ? config.templateModuleName : ''
        templateFolder = config.containsKey('templateFolder') ? config.templateFolder : 'templates'

        if (config.containsKey('moduleBaseName')) {
            moduleBaseName = config.moduleBaseName
        }
        else if (config.containsKey('moduleNameBase')) {
            // This is an alias because of a typo in the docs
            moduleBaseName = config.moduleNameBase
        }
        else {
            moduleBaseName = ''
        }

        preserveLineBreaks = config.containsKey('preserveLineBreaks') ? config.preserveLineBreaks : false
        convertUnderscores = config.containsKey('convertUnderscores') ? config.convertUnderscores : true
        includePathInName = config.containsKey('includePathInName') ? config.includePathInName : true
        compressHtml = config.containsKey('compressHtml') ? config.compressHtml : true
        preserveHtmlComments = config.containsKey('preserveHtmlComments') ? config.preserveHtmlComments : false
    }
}
