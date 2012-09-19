package org.grails.cxf.utils

import org.grails.cxf.artefact.EndpointArtefactHandler
import org.codehaus.groovy.grails.commons.GrailsApplication

/**
 *
 */
class GrailsCxfUtils {

    static final String CFG_LOAD_ON_STARTUP = 'servlet.loadOnStartup'
    static final String CFG_DEFAULT_SERVLET = 'servlet.defaultServlet'
    static final String CFG_SERVLET_MAPPINGS = 'servlets'
    static final String CFG_ENDPOINT_SOAP12 = 'endpoint.soap12Binding'

    static GrailsApplication grailsApplication

    private GrailsCxfUtils() {
        // Class contains static methods only
    }

    static ConfigObject getCxfConfig() {
        return grailsApplication.config?.cxf
    }

    static NavigableConfiguration getCxfNavConfig() {
        return new NavigableConfiguration(cxfConfig)
    }

    static Object getConfig(String configPath) {
        cxfConfig?."${configPath}" ?: null
    }

    static void reloadCxfConfig() {
        CxfConfigHandler.instance.reloadCxfConfig()
    }

    static Integer getLoadOnStartup() {
        return getConfig(CFG_LOAD_ON_STARTUP) as Integer
    }

    static Map<String, String> getServletsMappings() {
        assert getConfig(CFG_SERVLET_MAPPINGS), "There must be at least one configured servlet."
        return getConfig(CFG_SERVLET_MAPPINGS) as Map<String, String>
    }

    static String getDefaultServletName() {
        Object defaultName = getConfig(CFG_DEFAULT_SERVLET)

        if(defaultName instanceof String && !defaultName.isEmpty() &&
           servletsMappings.containsKey(defaultName)) {
            return defaultName
        }

        return new TreeMap<String, String>(servletsMappings).firstKey()
    }

    static Boolean getDefaultSoap12Binding() {
        return getConfig(CFG_ENDPOINT_SOAP12) as Boolean
    }

    static List configuredArtefacts() {
        return [EndpointArtefactHandler]
    }

    static String flexibleEnumName(String name) {
        return name.replaceAll(/(\s|\-)/, '_').toUpperCase()
    }
}
