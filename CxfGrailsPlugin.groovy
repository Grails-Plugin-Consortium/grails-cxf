import org.codehaus.groovy.grails.commons.GrailsClassUtils

class CxfGrailsPlugin {
    // the plugin version
    def version = "1.0.0"
    // the version or versions of Grails the plugin is designed for
    def grailsVersion = "2.0.0 > *"
    // the other plugins this plugin depends on
    def loadAfter = ['hibernate']
    def observe = ['hibernate']
    def dependsOn = [hibernate: '2.0.0 > *']

    // resources that are excluded from plugin packaging
    def pluginExcludes = [
            "grails-app/views/error.gsp"
    ]

    def author = "Ryan Crum"
    def authorEmail = "ryan.j.crum@gmail.com"
    def title = "CXF plug-in for Grails"
    def description = 'Add SOAP exposure to Grails services using Apache CXF.'
    def developers = [
            [name: "Ryan Crum", email: "ryan.j.crum@gmail.com"],
            [name: "Christian Oestreich", email: "acetrike@gmail.com"]]

    // URL to the plugin's documentation
    def documentation = "http://grails.org/plugin/cxf"

    def doWithSpring = {
        //In 2.5.2 the CXFServlet has some hard coded paths to "/WEB-INF/cxf-servlet.xml"
        //probably don't want to create that file, so just wire up the cxf bean here.
        "cxf"(org.apache.cxf.bus.spring.SpringBus)


        if(application.serviceClasses) {
            application.serviceClasses.each { service ->
                def srvClass = service.getClazz()
                def exposes = GrailsClassUtils.getStaticPropertyValue(srvClass, 'expose')
                if(exposes?.contains('cxf')) {
                    def wsName = service.propertyName
                    "${wsName}Factory"(org.grails.cxf.GrailsCXFServerFactoryBean, wsName, srvClass) {
                        address = "/${wsName.replace("Service", "")}"
                        serviceBean = ref("${service.propertyName}")
                    }
                } else if(exposes?.contains('cxfjax')) { // can't do both!
                    def wsName = service.propertyName
                    "${wsName}Factory"(org.grails.cxf.GrailsCXFJaxWsServerFactoryBean, wsName, srvClass) {
                        address = "/${wsName.replace("Service", "")}"
                        serviceBean = ref("${service.propertyName}")
                    }
                } else if(exposes?.contains('cxfrs')) { // can't do both!
                    def wsName = service.propertyName
                    "${wsName}Factory"(org.grails.cxf.GrailsCXFRSServerFactoryBean, wsName, srvClass) {
                        address = "/${wsName.replace("Service", "")}"
                        serviceBeans = [ref("${service.propertyName}")]
                    }

                }
            }
        }

    }

    def doWithApplicationContext = { applicationContext ->
    }

    def doWithWebDescriptor = { xml ->
        def filters = xml.filter
        def filterMappings = xml.'filter-mapping'
        def servlets = xml.servlet
        def servletMappings = xml.'servlet-mapping'

        servlets[servlets.size() - 1] + {
            servlet {
                'servlet-name'('CXFServlet')
                'servlet-class'('org.grails.cxf.GrailsCXFServlet')
                'load-on-startup'(1)
            }
        }

        servletMappings[servletMappings.size() - 1] + {
            'servlet-mapping' {
                'servlet-name'('CXFServlet')
                'url-pattern'("/services/*")
            }
        }
    }

    def doWithDynamicMethods = { ctx ->
        // TODO Implement registering dynamic methods to classes (optional)
    }

    def onChange = { event ->
        // TODO Implement code that is executed when any artefact that this plugin is
        // watching is modified and reloaded. The event contains: event.source,
        // event.application, event.manager, event.ctx, and event.plugin.
    }

    def onConfigChange = { event ->
        // TODO Implement code that is executed when the project configuration changes.
        // The event is the same as for 'onChange'.
    }
}
