grails.project.work.dir = 'target'

grails.project.dependency.resolution = {

    inherits 'global'
    log 'warn'
    legacyResolve false

    String gebVersion = '0.9.0'
    String seleniumVersion = '2.39.0'
    String spockVersion = '0.7'

    repositories {
        grailsCentral()
        grailsPlugins()
        mavenCentral()
    }

    dependencies {
        test("org.seleniumhq.selenium:selenium-htmlunit-driver:$seleniumVersion") {
            excludes 'xml-apis'
        }

        test "org.spockframework:spock-grails-support:0.7-groovy-2.0"
        test "org.seleniumhq.selenium:selenium-support:$seleniumVersion"
        test "org.gebish:geb-spock:$gebVersion"

        test "org.seleniumhq.selenium:selenium-remote-driver:$seleniumVersion"
        test( "com.github.detro.ghostdriver:phantomjsdriver:1.0.3" ) {
            transitive = false
        }
        test "org.seleniumhq.selenium:selenium-firefox-driver:$seleniumVersion"

    }
    plugins {
        runtime ":asset-pipeline:1.8.4"

        build(":tomcat:$grailsVersion",
              ":release:2.2.1",
              ":rest-client-builder:1.0.3") {
            export = false
        }

        test ":geb:$gebVersion"
        test(":spock:$spockVersion") {
            exclude "spock-grails-support"
        }
    }
}
