grails.project.work.dir = 'target'
grails.project.source.level = 1.6

grails.project.dependency.resolution = {

    String cxfVersion = '2.6.2'
    String jaxbVersion = '2.2.6'
    String gebVersion = '0.7.2'

    inherits 'global'
    log 'warn'

    repositories {
        grailsCentral()
        mavenLocal()
        mavenCentral()
    }

    dependencies {
        compile('commons-cli:commons-cli:1.2')

        compile("org.apache.cxf:cxf-tools-wsdlto-core:${cxfVersion}") {
            excludes 'xmlbeans', 'spring-web', 'spring-core', 'xml-apis', 'jaxb-impl','jaxb-xjc'
        }

        compile("org.apache.cxf:cxf-tools-wsdlto-frontend-jaxws:${cxfVersion}") {
            excludes 'xmlbeans', 'spring-web', 'spring-core', 'xml-apis', 'jaxb-impl','jaxb-xjc'
        }

        compile("org.apache.cxf:cxf-tools-wsdlto-databinding-jaxb:${cxfVersion}") {
            excludes 'xmlbeans', 'spring-web', 'spring-core', 'xml-apis', 'jaxb-impl','jaxb-xjc'
        }

        compile("org.apache.cxf:cxf-rt-frontend-jaxws:${cxfVersion}") {
            excludes 'xmlbeans', 'spring-web', 'spring-core', 'xml-apis', 'jaxb-impl','jaxb-xjc'
        }

        compile("org.apache.cxf:cxf-rt-frontend-jaxrs:${cxfVersion}") {
            excludes 'xmlbeans', 'spring-web', 'spring-core', 'xml-apis', 'jaxb-impl','jaxb-xjc'
        }

        compile("com.sun.xml.bind:jaxb-impl:${jaxbVersion}")

        compile("com.sun.xml.bind:jaxb-xjc:${jaxbVersion}")

        /* Some Testing Help **************************************************/
        test("com.github.groovy-wslite:groovy-wslite:0.7.0") {
            export = false
        }

        test("org.codehaus.geb:geb-spock:${gebVersion}") {
            export = false
        }

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
    }

    plugins {
        //remove this before committing.  Only used to release...not test.
        // This still an issue?!?
        // http://grails.1312388.n4.nabble.com/Geb-and-Release-plugin-httpclient-conflicts-td4295238.html
//        build(":release:2.2.0", ':rest-client-builder:1.0.3') {
//            export = false
//        }

        runtime(":hibernate:${grailsVersion}") {
            export = false
        }

        runtime(":tomcat:${grailsVersion}") {
            export = false
        }

        /* Spock and Geb for Testing ******************************************/
        test(":spock:0.6") {
            export = false
        }

        test(":geb:${gebVersion}") {
            export = false
        }

        test(":code-coverage:1.2.5") {
            export = false
        }

        test(":codenarc:0.18") {
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
