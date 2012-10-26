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
                        'servlet-class'(pattern.clazz)
                        'load-on-startup'(loadOnStartup)
                    }
                }

                lastServletMappingDescriptor() + {
                    'servlet-mapping' {
                        'servlet-name'(name)
                        'url-pattern'(pattern.url)
                    }
                }
            }
        }
    }

    private void assertServletConfig(String name, Map pattern) {
        assert name, 'Servlet configuration requires a name.'
        assert pattern.url, 'Servlet configuration requires a url-pattern.'
        assert pattern.clazz, 'Servlet configuration requires a class.'

        log.debug "Creating web.xml entries for servlet [$name] at [${pattern.url}] and class [${pattern.clazz}]."
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
