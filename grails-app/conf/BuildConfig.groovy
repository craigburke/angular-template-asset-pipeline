grails.project.work.dir = 'target'

grails.project.fork = false
grails.project.dependency.resolver = "maven"

grails.project.dependency.resolution = {

    inherits 'global'
    log 'warn'
    legacyResolve false

    String gebVersion = '0.9.2'
    String seleniumVersion = '2.43.1'
    String spockVersion = '0.7'

    repositories {
        grailsCentral()
        grailsPlugins()
        mavenCentral()
    }

    dependencies {
		compile "com.craigburke.angular:angular-template-asset-pipeline:2.0.1"
    }
    plugins {
       
        build(":release:3.0.1",
              ":rest-client-builder:1.0.3",
              ":tomcat:7.0.53") {
            export = false
		}
    }
}
