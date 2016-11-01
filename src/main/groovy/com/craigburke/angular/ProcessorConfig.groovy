package com.craigburke.angular

import groovy.transform.CompileStatic

@CompileStatic
class ProcessorConfig {

    final String templateFolder
    final String moduleBaseName
    final Boolean convertUnderscores
    final Boolean includePathInName
    final Boolean compressHtml
    final Boolean preserveHtmlComments

    ProcessorConfig(Map config = [:]) {
        templateFolder = config.containsKey('templateFolder') ? config.templateFolder : 'templates'
        moduleBaseName = config.containsKey('moduleBaseName') ? config.moduleBaseName : ''
        convertUnderscores = config.containsKey('convertUnderscores') ? config.convertUnderscores : true
        includePathInName = config.containsKey('includePathInName') ? config.includePathInName : true
        compressHtml = config.containsKey('compressHtml') ? config.compressHtml : true
        preserveHtmlComments = config.containsKey('preserveHtmlComments') ? config.preserveHtmlComments : false
    }
}
