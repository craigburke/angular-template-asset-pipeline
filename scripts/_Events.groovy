eventAssetPrecompileStart = { assetConfig ->
    assetConfig.specs << "com.craigburke.angular.HtmlTemplateAssetFile"
    assetConfig.specs << "com.craigburke.angular.GspTemplateAssetFile"
}