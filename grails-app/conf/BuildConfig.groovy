grails.project.work.dir = "target"
grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"

grails.project.dependency.resolution = {

    inherits("global") {}

    log "warn"

    repositories {
        grailsPlugins()
        grailsHome()
        grailsCentral()
        mavenCentral()
    }
    dependencies {
        compile('org.apache.cxf:cxf-rt-frontend-jaxws:2.6.1') {
            excludes 'spring-web'
        }
        compile('org.apache.cxf:cxf-rt-frontend-jaxrs:2.6.1') {
            excludes 'xmlbeans', 'spring-web', 'spring-core'
        }
    }

    plugins {
        runtime 'org.grails.plugins:hibernate:1.3.7', { export: false }
        runtime 'org.grails.plugins:tomcat:1.3.7', { export: false}
        test 'org.grails.plugins:spock:0.5-groovy-1.7', { export: false }
    }
}

