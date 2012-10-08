grails.project.work.dir = 'target'
grails.project.class.dir = 'target/classes'
grails.project.test.class.dir = 'target/test-classes'
grails.project.test.reports.dir = 'target/test-reports'

grails.project.dependency.resolution = {

    def exportLibs = { export: false }
    def excludeConflicting = { excludes 'xmlbeans', 'spring-web', 'spring-core', 'xml-apis' }

    def cxfGroup = 'org.apache.cxf'
    def cxfVersion = '2.6.1'

    def gebVersion = '0.7.2'

    def pluginsGroup = 'org.grails.plugins'
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

        build name: 'commons-cli',
              version: '1.2',
              group: 'commons-cli'

        /* Dependencies for the Wsdl To Java script ***************************/
        build name: 'cxf-tools-wsdlto-core',
              version: cxfVersion,
              group: cxfGroup,
              excludeConflicting

        build name: 'cxf-tools-wsdlto-frontend-jaxws',
              version: cxfVersion,
              group: cxfGroup,
              excludeConflicting

        build name: 'cxf-tools-wsdlto-databinding-jaxb',
              version: cxfVersion,
              group: cxfGroup,
              excludeConflicting

        /* Dependencies for the Cxf Runtime ***********************************/
        compile name: 'cxf-rt-frontend-jaxws',
                version: cxfVersion,
                group: cxfGroup,
                excludeConflicting

        compile name: 'cxf-rt-frontend-jaxrs',
                version: cxfVersion,
                group: cxfGroup,
                excludeConflicting

        /* Some Testing Help **************************************************/
        test name: 'groovy-wslite',
             version: '0.7.0',
             group: 'com.github.groovy-wslite',
             exportLibs

        test name: 'geb-spock',
             version: gebVersion,
             group: 'org.codehaus.geb',
             exportLibs

        test name: 'selenium-htmlunit-driver',
             version: '2.25.0',
             group: 'org.seleniumhq.selenium', {
                    with exportLibs
                    with excludeConflicting
                }

        test name: 'selenium-firefox-driver',
             version: '2.20.0',
             group: 'org.seleniumhq.selenium',
             exportLibs

        test name: 'selenium-chrome-driver',
             version: '2.20.0',
             group: 'org.seleniumhq.selenium',
             exportLibs
    }

    plugins {
        /* Grails required plugins ********************************************/
        // WTF... this still an issue?!?
        // http://grails.1312388.n4.nabble.com/Geb-and-Release-plugin-httpclient-conflicts-td4295238.html
//        build name: 'release',
//              version: '2.0.4',
//              group: pluginsGroup,
//              {
//                  with exportLibs
//                  with excludeHttpBuilder
//              }

        runtime name: 'hibernate',
                version: grailsVersion,
                group: pluginsGroup,
                exportLibs

        runtime name: 'tomcat',
                version: grailsVersion,
                group: pluginsGroup,
                exportLibs

        /* Spock and Geb for Testing ******************************************/
        test name: 'spock',
             version: '0.6',
             group: pluginsGroup,
             exportLibs

        test name: 'geb',
             version: gebVersion,
             group: pluginsGroup,
             exportLibs

        test name: 'code-coverage',
             version: '1.2.5',
             group: pluginsGroup,
             exportLibs

        test name: 'codenarc',
             version: '0.17',
             group: pluginsGroup,
             exportLibs
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
