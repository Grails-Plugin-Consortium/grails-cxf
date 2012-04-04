grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"
//grails.project.war.file = "target/${appName}-${appVersion}.war"
grails.project.dependency.resolution = {
    // inherit Grails' default dependencies
    inherits("global") {
        // uncomment to disable ehcache
        // excludes 'ehcache'
    }
    log "warn" // log level of Ivy resolver, either 'error', 'warn', 'info', 'debug' or 'verbose'
    repositories {
        grailsPlugins()
        grailsHome()
        grailsCentral()

        // uncomment the below to enable remote dependency resolution
        // from public Maven repositories
        //mavenLocal()
        mavenCentral()
        //mavenRepo "http://snapshots.repository.codehaus.org"
        //mavenRepo "http://repository.codehaus.org"
        //mavenRepo "http://download.java.net/maven/2/"
        //mavenRepo "http://repository.jboss.com/maven2/"
    }
    dependencies {
        // specify dependencies here under either 'build', 'compile', 'runtime', 'test' or 'provided' scopes eg.
        //compile('org.apache.cxf:cxf-rt-transports-http:2.3.0'){
        //	excludes 'spring-web'
        //}
        compile('org.apache.cxf:cxf-rt-frontend-jaxws:2.5.2') {
            excludes 'spring-web'
        }
        compile('org.apache.cxf:cxf-rt-frontend-jaxrs:2.5.2') {
            excludes 'xmlbeans', 'spring-web', 'spring-core'
        }
        // runtime 'mysql:mysql-connector-java:5.1.5'
    }
}

