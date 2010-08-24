import org.codehaus.groovy.grails.commons.GrailsClassUtils
import org.apache.cxf.frontend.ServerFactoryBean
import org.apache.cxf.transport.DestinationFactory
import org.apache.cxf.transport.DestinationFactoryManager
import org.apache.cxf.transport.servlet.ServletTransportFactory
import org.apache.cxf.bus.spring.SpringBusFactory
import org.apache.cxf.transport.servlet.CXFServlet
import org.apache.cxf.Bus
//import org.grails.cxf.CXFController
import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping

class CxfGrailsPlugin {
    // the plugin version
    def version = "0.5.1"
    // the version or versions of Grails the plugin is designed for
    def grailsVersion = "1.1.1 > *"
    // the other plugins this plugin depends on
    def loadAfter = ['hibernate']
    def observe = ['hibernate']
    def dependsOn = [hibernate: '1.1 > *']
    
    // resources that are excluded from plugin packaging
    def pluginExcludes = [
            "grails-app/views/error.gsp"
    ]

    def author = "Ryan Crum"
    def authorEmail = "ryan.j.crum@gmail.com"
    def title = "CXF plug-in for Grails"
    def description = '''\\
Add SOAP exposure to Grails services using Apache CXF.
'''

    // URL to the plugin's documentation
    def documentation = "http://grails.org/plugin/cxf"

    def doWithSpring = {
    if (application.serviceClasses) {
      application.serviceClasses.each { service ->
        def srvClass = service.getClazz()
        def exposes = GrailsClassUtils.getStaticPropertyValue(srvClass, 'expose')
        if (exposes?.contains('cxf')) {
          def wsName = service.propertyName
          "${wsName}Factory"(org.grails.cxf.GrailsCXFServerFactoryBean, wsName, srvClass) { 
            address = "/${wsName.replace("Service", "")}"
            serviceBean = ref("${service.propertyName}")
          }
        } else if (exposes?.contains('cxfjax')) { // can't do both!
          def wsName = service.propertyName
          "${wsName}Factory"(org.grails.cxf.GrailsCXFJaxWsServerFactoryBean, wsName, srvClass) { 
            address = "/${wsName.replace("Service", "")}"
            serviceBean = ref("${service.propertyName}")
          }
        } else if (exposes?.contains('cxfrs')) { // can't do both!
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

      def hibernateFilter = 'hibernateFilter'
      filters[filters.size() - 1] + {
        filter {
          'filter-name'(hibernateFilter)
          'filter-class'('org.codehaus.groovy.grails.orm.hibernate.support.GrailsOpenSessionInViewFilter')
        }
      }

      filterMappings[filterMappings.size() - 1] + {
        'filter-mapping' {
          'filter-name'(hibernateFilter)
          'url-pattern'("/services/*")
        }
      }

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
