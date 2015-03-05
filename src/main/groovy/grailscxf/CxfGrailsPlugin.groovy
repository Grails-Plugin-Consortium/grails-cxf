package grailscxf

import grails.plugins.Plugin
import groovy.util.logging.Commons
import org.grails.cxf.artefact.EndpointBeanConfiguration
import org.grails.cxf.servlet.GrailsCxfServlet
import org.grails.cxf.utils.GrailsCxfUtils
import org.springframework.boot.context.embedded.ServletRegistrationBean

@Commons
class CxfGrailsPlugin extends Plugin {

    /* **********************************************************************
     * The important things first.
     * **********************************************************************/
    def profiles = ['web']

    def artefacts = GrailsCxfUtils.configuredArtefacts()

    Closure doWithSpring() {
        { ->
            EndpointBeanConfiguration bc = new EndpointBeanConfiguration(application)

            with bc.cxfBeans()
            with bc.endpointBeans()
            with bc.factoryBeans()


            cxfServlet(ServletRegistrationBean, new GrailsCxfServlet(), "/services/*") {
                loadOnStartup = 2
            }
        }
    }

    /* **********************************************************************
     * Plugin Metadata Down-under
     * **********************************************************************/

    def author = 'Grails Plugin Consortium'
    def authorEmail = 'acetrike@gmail.com'
    def title = 'CXF plug-in for Grails'
    def description = 'Brings easy exposure of service and endpoint classes as Apache CXF SOAP Services to Grails.'
    def grailsVersion = "3.0.0.BUILD-SNAPSHOT > *"

    def developers = [
            [name: "Christian Oestreich", email: "acetrike@gmail.com"],
            [name: "Ryan Crum", email: "ryan.j.crum@gmail.com"],
            [name: "Ben Doerr", email: "craftsman@bendoerr.me"]]

    def documentation = "https://github.com/Grails-Plugin-Consortium/grails-cxf"
    def license = 'APACHE'

    def issueManagement = [system: 'JIRA', url: 'https://github.com/Grails-Plugin-Consortium/grails-cxf/issues']
    def scm = [url: "https://github.com/Grails-Plugin-Consortium/grails-cxf"]

    def pluginExcludes = [
            'grails-app/conf/spring/ApplicationResources.groovy',
            'grails-app/conf/spring/resources.groovy',
            'grails-app/conf/spring/resources.groovy',
            'grails-app/conf/ApplicationResources.groovy',
            'grails-app/conf/BootStrap.groovy',
            'grails-app/conf/DataSource.groovy',
            'grails-app/conf/UrlMappings.groovy',
            'grails-app/conf/codenarc.groovy',
            'grails-app/conf/codenarc.ruleset.all.groovy.txt',
            'grails-app/domain/**',
            'grails-app/endpoints/**',
            'grails-app/i18n/**',
            'grails-app/services/**',
            'grails-app/views/**',
            'src/main/groovy/org/grails/cxf/test/**',
            'src/test/**',
            'web-app/**',
            'codenarc.properties'
    ]
}
