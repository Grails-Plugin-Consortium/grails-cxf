package org.grails.cxf.servlet

import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory
import org.grails.cxf.utils.GrailsCxfUtils

/**
 * The configuration for the Web Descriptor (web.xml) that adds in our servlets.
 */
class WebDescriptorConfiguration {

    Object webXml
    private static final Log log = LogFactory.getLog(WebDescriptorConfiguration)

    WebDescriptorConfiguration(final Object webXml) {
        this.webXml = webXml
    }

    /**
     * @return the web xml dsl for our Cxf servlets.
     */
    Closure configuredServlets() {
        return {
            GrailsCxfUtils.servletsMappings.each {name, pattern ->
                assertServletConfig(name, pattern)

                lastServletDescriptor() + {
                    servlet {
                        'servlet-name'(name)
                        'servlet-class'('org.grails.cxf.servlet.GrailsCxfServlet')
                        'load-on-startup'(loadOnStartup)
                    }
                }

                lastServletMappingDescriptor() + {
                    'servlet-mapping' {
                        'servlet-name'(name)
                        'url-pattern'(pattern)
                    }
                }
            }
        }
    }

    private void assertServletConfig(String name, String pattern) {
        assert name, 'Servlet configuration requires a name.'
        assert pattern, 'Servlet configuration requires a url-pattern.'

        log.debug "Creating web.xml entries for servlet [$name] at [$pattern]."
    }

    private Integer getLoadOnStartup() {
        GrailsCxfUtils.loadOnStartup
    }

    private lastServletDescriptor() {
        def servlets = webXml.servlet
        servlets[-1]
    }

    private lastServletMappingDescriptor() {
        def servletMappings = webXml.'servlet-mapping'
        servletMappings[-1]
    }
}
