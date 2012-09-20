import org.grails.cxf.artefact.EndpointBeanConfiguration
import org.grails.cxf.servlet.WebDescriptorConfiguration
import org.grails.cxf.utils.GrailsCxfUtils
import grails.util.GrailsUtil

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

    def doWithApplicationContext = { applicationContext ->
    }

    def doWithDynamicMethods = { ctx ->
    }

    def watchedResources = [
            'file:./grails-app/endpoints/**/*',
            'file:./grails-app/services/**/*',
            'file:./grails-app/conf/*Config.groovy'
    ]

    def onChange = { event ->
        // TODO Implement code that is executed when any artefact that this plugin is
        // watching is modified and reloaded. The event contains: event.source,
        // event.application, event.manager, event.ctx, and event.plugin.
    }

    def onConfigChange = { event ->
        // TODO Implement code that is executed when the project configuration changes.
        // The event is the same as for 'onChange'.
    }

    /* **********************************************************************
     * Plugin Metadata Down-under
     * **********************************************************************/

    def version = '1.0.0'
    def grailsVersion = '1.3.7 > *'

    def author = 'Ryan Crum'
    def authorEmail = 'ryan.j.crum@gmail.com'
    def title = 'CXF Server and Client plug-in for Grails'
    def description = ''

    def developers = [
            [name: "Ryan Crum", email: "ryan.j.crum@gmail.com"],
            [name: "Christian Oestreich", email: "acetrike@gmail.com"],
            [name: "Ben Doerr", email: "craftsman@bendoerr.me"]]

    def documentation = "http://grails.org/plugin/cxf"
    def license = ''

    def issueManagement = [system: 'JIRA', url: '']
    def scm = [url: '']

    def loadAfter = ['hibernate']                   // TODO: Is hibernate really necessary?
    def observe = ['hibernate']                     // Maybe in the future we add some logging domain class?
    def dependsOn = [hibernate: '1.3.7 > *']        // But really right now who cares?

    def pluginExcludes = [
            'grails-app/conf/hibernate',
            'grails-app/conf/spring',
            'grails-app/conf/DataSource.groovy',
            'grails-app/conf/UrlMappings.groovy',
            'grails-app/conf/codenarc.groovy',
            'grails-app/conf/codenarc.ruleset.all.groovy.txt',
            'grails-app/controllers/**',
            'grails-app/domain/**',
            'grails-app/endpoints/**',
            'grails-app/i18n/**',
            'grails-app/services/**',
            'grails-app/taglib/**',
            'grails-app/utils/**',
            'grails-app/views/**',
            'src/groovy/org/grails/cxf/test/**',
            'src/java/org/grails/cxf/test/**',
            'lib/**',
            'target/**',
            'web-app/**',
            'codenarc.properties'
    ]
}
