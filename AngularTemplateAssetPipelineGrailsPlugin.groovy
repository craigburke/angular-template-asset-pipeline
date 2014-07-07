import asset.pipeline.AssetHelper
import com.craigburke.angular.TemplateAssetFile

class AngularTemplateAssetPipelineGrailsPlugin {

    def version = "1.0.2"
    def grailsVersion = "2.0 > *"

    def pluginExcludes = [
        "grails-app/assets/**"
    ]

    def title = "AngularJS Template Asset-Pipeline Plugin"
    def author = "Craig Burke"
    def authorEmail = "craig@craigburke.com"
    def description = "Provides AngularJS template support for the asset-pipeline static asset management plugin."
    def documentation = "http://github.com/craigburke/angular-template-asset-pipeline"

    def license = "APACHE"
    def issueManagement = [ system: "GITHUB", url: "http://github.com/craigburke/angular-template-asset-pipeline/issues" ]
    def scm = [ url: "http://github.com/craigburke/angular-template-asset-pipeline" ]

    def doWithDynamicMethods = { ctx ->
        AssetHelper.assetSpecs << TemplateAssetFile
    }

}
