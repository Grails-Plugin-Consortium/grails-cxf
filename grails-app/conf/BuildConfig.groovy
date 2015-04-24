grails.servlet.version = "3.0" // Change depending on target container compliance (2.5 or 3.0)
grails.project.work.dir = 'target'
grails.project.source.level = 1.6

grails.project.dependency.resolver = "maven" // or ivy
grails.project.dependency.resolution = {

    String cxfVersion = '3.0.4'
    String jaxbVersion = '2.2.11'
    String springVersion = '4.0.9.RELEASE'

    inherits 'global'
    log 'warn'
    checksums true // Whether to verify checksums on resolve
    legacyResolve false // whether to do a secondary resolve on plugin installation, not advised and here for backwards compatibility

    repositories {
        inherits true // Whether to inherit repository definitions from plugins

        mavenLocal()
        mavenCentral()
        grailsPlugins()
        grailsHome()
        grailsCentral()
//        mavenRepo "http://repo.grails.org/grails/repo/"
//        mavenRepo "http://repo1.maven.org/maven2/"
    }

    dependencies {
        compile('commons-cli:commons-cli:1.2')

        compile("org.apache.cxf:cxf-core:${cxfVersion}") {
            excludes 'xmlbeans', 'spring-web', 'spring-core', 'xml-apis', 'jaxb-impl', 'jaxb-xjc'
        }

        compile("org.apache.cxf:cxf-tools-wsdlto-core:${cxfVersion}") {
            excludes 'xmlbeans', 'spring-web', 'spring-core', 'xml-apis', 'jaxb-impl', 'jaxb-xjc'
        }

        compile("org.apache.cxf:cxf-tools-wsdlto-frontend-jaxws:${cxfVersion}") {
            excludes 'xmlbeans', 'spring-web', 'spring-core', 'xml-apis', 'jaxb-impl', 'jaxb-xjc'
        }

        compile("org.apache.cxf:cxf-tools-wsdlto-databinding-jaxb:${cxfVersion}") {
            excludes 'xmlbeans', 'spring-web', 'spring-core', 'xml-apis', 'jaxb-impl', 'jaxb-xjc'
        }

        compile("org.apache.cxf:cxf-rt-frontend-jaxws:${cxfVersion}") {
            excludes 'xmlbeans', 'spring-web', 'spring-core', 'xml-apis', 'jaxb-impl', 'jaxb-xjc'
        }

        compile("org.apache.cxf:cxf-rt-frontend-jaxrs:${cxfVersion}") {
            excludes 'xmlbeans', 'spring-web', 'spring-core', 'xml-apis', 'jaxb-impl', 'jaxb-xjc'
        }
        compile("org.apache.cxf:cxf-core:${cxfVersion}") {
            excludes 'xmlbeans', 'spring-web', 'spring-core', 'xml-apis', 'jaxb-impl', 'jaxb-xjc'
        }
        compile("org.apache.cxf:cxf-rt-frontend-jaxws:${cxfVersion}") {
            excludes 'xmlbeans', 'spring-web', 'spring-core', 'xml-apis', 'jaxb-impl', 'jaxb-xjc'
        }
        compile("org.apache.cxf:cxf-rt-frontend-jaxrs:${cxfVersion}") {
            excludes 'xmlbeans', 'spring-web', 'spring-core', 'xml-apis', 'jaxb-impl', 'jaxb-xjc'
        }
        compile("org.apache.cxf:cxf-rt-frontend-simple:${cxfVersion}") {
            excludes 'xmlbeans', 'spring-web', 'spring-core', 'xml-apis', 'jaxb-impl', 'jaxb-xjc'
        }
        compile("org.apache.cxf:cxf-rt-wsdl:${cxfVersion}") {
            excludes 'xmlbeans', 'spring-web', 'spring-core', 'xml-apis', 'jaxb-impl', 'jaxb-xjc'
        }
        compile("org.apache.cxf:cxf-rt-bindings-soap:${cxfVersion}") {
            excludes 'xmlbeans', 'spring-web', 'spring-core', 'xml-apis', 'jaxb-impl', 'jaxb-xjc'
        }
        compile("org.apache.cxf:cxf-rt-databinding-jaxb:${cxfVersion}") {
            excludes 'xmlbeans', 'spring-web', 'spring-core', 'xml-apis', 'jaxb-impl', 'jaxb-xjc'
        }

        compile("com.sun.xml.bind:jaxb-impl:${jaxbVersion}")

        compile("com.sun.xml.bind:jaxb-xjc:${jaxbVersion}")

        compile("org.springframework:spring-expression:${springVersion}")

        compile("org.springframework:spring-aop:${springVersion}")

        /* Some Testing Help **************************************************/
//        test('org.apache.ws.security:wss4j:1.6.18') {
        /*
        <module>parent</module>
        <module>bindings</module>
        <module>policy</module>
        <module>ws-security-common</module>
        <module>ws-security-dom</module>
        <module>ws-security-stax</module>
        <module>integration</module>
        <module>ws-security-policy-stax</module>
        */
        test('org.apache.wss4j:wss4j-ws-security-stax:2.0.3') {
            excludes 'xmlbeans', 'spring-web', 'spring-core', 'xml-apis','junit', 'log4j', 'slf4j', 'slf4j-log4j12', 'slf4j-api', 'slf4j-jdk14'
            export = false
        }

        test('org.apache.wss4j:wss4j-ws-security-dom:2.0.3') {
            excludes 'xmlbeans', 'spring-web', 'spring-core', 'xml-apis','junit', 'log4j', 'slf4j', 'slf4j-log4j12', 'slf4j-api', 'slf4j-jdk14'
            export = false
        }

        test('org.apache.wss4j:wss4j-bindings:2.0.3') {
            excludes 'xmlbeans', 'spring-web', 'spring-core', 'xml-apis','junit', 'log4j', 'slf4j', 'slf4j-log4j12', 'slf4j-api', 'slf4j-jdk14'
            export = false
        }

        test('org.apache.wss4j:wss4j-policy:2.0.3') {
            excludes 'xmlbeans', 'spring-web', 'spring-core', 'xml-apis','junit', 'log4j', 'slf4j', 'slf4j-log4j12', 'slf4j-api', 'slf4j-jdk14'
            export = false
        }

        test("org.apache.cxf:cxf-rt-ws-security:${cxfVersion}") {
            excludes 'xmlbeans', 'spring-web', 'spring-core', 'xml-apis',
                    'ehcache', 'easymock', 'ehcache-core',
                    'log4j', 'slf4j', 'slf4j-log4j12', 'slf4j-api', 'slf4j-jdk14'
            export = false
        }


        test("org.gebish:geb-spock:0.10.0") {
            export = false
        }

//        test("com.github.groovy-wslite:groovy-wslite:0.7.2.0") {
//            export = false
//        }

//        test("org.codehaus.geb:geb-spock:${gebVersion}") {
//            export = false
//        }

        test('org.seleniumhq.selenium:selenium-htmlunit-driver:2.25.0') {
            excludes 'xmlbeans', 'spring-web', 'spring-core', 'xml-apis'
            export = false
        }

        test('org.seleniumhq.selenium:selenium-chrome-driver:2.25.0') {
            excludes 'xmlbeans', 'spring-web', 'spring-core', 'xml-apis'
            export = false
        }

        test('org.seleniumhq.selenium:selenium-firefox-driver:2.25.0') {
            excludes 'xmlbeans', 'spring-web', 'spring-core', 'xml-apis'
            export = false
        }

        test("org.grails:grails-datastore-test-support:1.0.2-grails-2.4") {
            export = false
        }
    }

    plugins {
        //remove this before committing.  Only used to release...not test.
        // This still an issue?!?
        // http://grails.1312388.n4.nabble.com/Geb-and-Release-plugin-httpclient-conflicts-td4295238.html
//        build(':release:3.0.1', ':rest-client-builder:2.0.1') {
//            export = false
//        }

        runtime(":hibernate:3.6.10.16") {
            export = false
        }

        runtime(":tomcat:7.0.53") {
            export = false
        }

        test(":wslite:0.7.2.0") {
            export = false
        }

        test(":geb:0.10.0") {
            export = false
        }

        test(":code-coverage:1.2.5") {
            export = false
        }

        test(":codenarc:0.21") {
            export = false
        }
    }
}

coverage {
    xml = true
    exclusions = ['**/*Tests*']
}

codenarc {
    processTestUnit = false
    processTestIntegration = false
    processServices = false
    processDomain = false
    propertiesFile = 'codenarc.properties'
    ruleSetFiles = 'file:grails-app/conf/codenarc.groovy'
    reports = {
        CxfClientReport('xml') {                    // The report name 'MyXmlReport' is user-defined; Report type is 'xml'
            outputFile = 'target/codenarc.xml'      // Set the 'outputFile' property of the (XML) Report
            title = 'Grails CXF Plugin'             // Set the 'title' property of the (XML) Report
        }
    }
}
