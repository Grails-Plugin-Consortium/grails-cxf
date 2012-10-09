grails.project.work.dir = 'target'
grails.project.class.dir = 'target/classes'
grails.project.test.class.dir = 'target/test-classes'
grails.project.test.reports.dir = 'target/test-reports'

grails.project.dependency.resolution = {

    def cxfVersion = '2.6.1'
    def gebVersion = '0.7.2'
    def grailsVersion = '2.1.0'

    inherits('global') {}

    log 'warn'

    repositories {
        grailsPlugins()
        grailsHome()
        grailsCentral()
        mavenCentral()
    }

    dependencies {
        compile('commons-cli:commons-cli:1.2')

        compile("org.apache.cxf:cxf-tools-wsdlto-core:${cxfVersion}") {
            excludes 'xmlbeans', 'spring-web', 'spring-core', 'xml-apis'
        }

        compile("org.apache.cxf:cxf-tools-wsdlto-frontend-jaxws:${cxfVersion}") {
            excludes 'xmlbeans', 'spring-web', 'spring-core', 'xml-apis'
        }

        compile("org.apache.cxf:cxf-tools-wsdlto-databinding-jaxb:${cxfVersion}") {
            excludes 'xmlbeans', 'spring-web', 'spring-core', 'xml-apis'
        }

        compile("org.apache.cxf:cxf-rt-frontend-jaxws:${cxfVersion}") {
            excludes 'xmlbeans', 'spring-web', 'spring-core', 'xml-apis'
        }

        compile("org.apache.cxf:cxf-rt-frontend-jaxrs:${cxfVersion}") {
            excludes 'xmlbeans', 'spring-web', 'spring-core', 'xml-apis'
        }

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

        test('org.seleniumhq.selenium:selenium-chrome-driver:2.20.0') {
            excludes 'xmlbeans', 'spring-web', 'spring-core', 'xml-apis'
            export = false
        }

        test('org.seleniumhq.selenium:selenium-firefox-driver:2.20.0') {
            excludes 'xmlbeans', 'spring-web', 'spring-core', 'xml-apis'
            export = false
        }
    }

    plugins {
        //remove this before committing.  Only used to release...not test.
        // This still an issue?!?
        // http://grails.1312388.n4.nabble.com/Geb-and-Release-plugin-httpclient-conflicts-td4295238.html
//        build(":release:2.0.4") {
//            export = false
//        }

        /* Grails required plugins ********************************************/
        runtime(":hibernate:${grailsVersion}") {
            export = false
        }

        runtime(":tomcat:${grailsVersion}") {
            export = false
        }

        /* Spock and Geb for Testing ******************************************/
        compile(":spock:0.6") {
            export = false
        }

        compile(":geb:${gebVersion}") {
            export = false
        }

        compile(":code-coverage:1.2.5") {
            export = false
        }

        compile(":codenarc:0.17") {
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
    propertiesFile = 'codenarc.properties'
    ruleSetFiles = 'file:grails-app/conf/codenarc.groovy'
    reports = {
        CxfClientReport('xml') {                    // The report name 'MyXmlReport' is user-defined; Report type is 'xml'
            outputFile = 'target/codenarc.xml'      // Set the 'outputFile' property of the (XML) Report
            title = 'Grails CXF Plugin'             // Set the 'title' property of the (XML) Report
        }
    }
}
