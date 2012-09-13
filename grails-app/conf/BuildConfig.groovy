grails.project.work.dir = "target"
grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"

grails.project.dependency.resolution = {

    def exportFalse = { export: false }
    def excludeConflicting = { excludes 'xmlbeans', 'spring-web', 'spring-core', 'xml-apis' }

    def cxfGroup = 'org.apache.cxf'
    def cxfVersion = '2.6.1'

    def pluginsGroup = 'org.grails.plugins'
    def grailsVersion = '1.3.7'

    inherits("global") {}

    log "warn"

    repositories {
        grailsPlugins()
        grailsHome()
        grailsCentral()
        mavenCentral()
    }

    dependencies {

        build   name: 'commons-cli',
                version:  '1.2',
                group: 'commons-cli'

        /* Dependencies for the Wsdl To Java script ***************************/
        build   name: 'cxf-tools-wsdlto-core',
                version: cxfVersion,
                group: cxfGroup,
                excludeConflicting

        build   name: 'cxf-tools-wsdlto-frontend-jaxws',
                version: cxfVersion,
                group: cxfGroup,
                excludeConflicting

        build   name: 'cxf-tools-wsdlto-databinding-jaxb',
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
        test    name: 'groovy-wslite',
                version: '0.7.0',
                group: 'com.github.groovy-wslite',
                exportFalse

        test    name: 'geb-spock',
                version: '0.5.1',
                group: 'org.codehaus.geb',
                exportFalse

        test    name: 'selenium-htmlunit-driver',
                version: '2.20.0',
                group: 'org.seleniumhq.selenium', {
                    with exportFalse
                    with excludeConflicting
                }

        test    name: 'selenium-chrome-driver',
                version: '2.20.0',
                group: 'org.seleniumhq.selenium',
                exportFalse
    }

    plugins {
        /* Grails required plugins ********************************************/
        runtime name: 'hibernate',
                version: grailsVersion,
                group: pluginsGroup,
                exportFalse

        runtime name: 'tomcat',
                version: grailsVersion,
                group: pluginsGroup,
                exportFalse

        /* Spock and Geb for Testing ******************************************/
        test    name: 'spock',
                version: '0.5-groovy-1.7',
                group: pluginsGroup,
                exportFalse

        test    name: 'geb',
                version: '0.5.1',
                group: pluginsGroup,
                exportFalse
    }
}

