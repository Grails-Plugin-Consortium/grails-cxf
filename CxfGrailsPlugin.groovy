import org.grails.cxf.artefact.EndpointBeanConfiguration
import org.grails.cxf.servlet.WebDescriptorConfiguration
import org.grails.cxf.utils.GrailsCxfUtils

class CxfGrailsPlugin {

    /* **********************************************************************
     * The important things first.
     * **********************************************************************/

    def doWithWebDescriptor = { xml ->
        WebDescriptorConfiguration wdc = new WebDescriptorConfiguration(xml)
        with wdc.configuredServlets()
    }

    def classes = []

    def artefacts = GrailsCxfUtils.configuredArtefacts()

    def doWithSpring = {
        EndpointBeanConfiguration bc = new EndpointBeanConfiguration(application)

        with bc.cxfBeans()
        with bc.endpointBeans()
        with bc.factoryBeans()
    }

    /* **********************************************************************
     * Plugin Metadata Down-under
     * **********************************************************************/

    def version = '1.1.1'
    def grailsVersion = '1.3.7 > *'

    def author = 'Grails Plugin Consortium'
    def authorEmail = ''
    def title = 'CXF plug-in for Grails'
    def description = 'Brings easy exposure of service and endpoint classes as Apache CXF SOAP Services to Grails.'

    def developers = [
            [name: "Christian Oestreich", email: "acetrike@gmail.com"],
            [name: "Ryan Crum", email: "ryan.j.crum@gmail.com"],
            [name: "Ben Doerr", email: "craftsman@bendoerr.me"]]

    def documentation = "https://github.com/Grails-Plugin-Consortium/grails-cxf"
    def license = 'APACHE'

    def issueManagement = [system: 'JIRA', url: 'https://github.com/Grails-Plugin-Consortium/grails-cxf/issues']
    def scm = [url: "https://github.com/Grails-Plugin-Consortium/grails-cxf"]

    def pluginExcludes = [
            'grails-app/conf/spring/resources.groovy',
            'grails-app/conf/codenarc.groovy',
            'grails-app/conf/codenarc.ruleset.all.groovy.txt',
            'grails-app/domain/**',
            'grails-app/endpoints/**',
            'grails-app/i18n/**',
            'grails-app/services/**',
            'grails-app/views/**',
            'src/groovy/org/grails/cxf/test/**',
            'src/java/org/grails/cxf/test/**',
            'web-app/**',
            'codenarc.properties'
    ]
}
