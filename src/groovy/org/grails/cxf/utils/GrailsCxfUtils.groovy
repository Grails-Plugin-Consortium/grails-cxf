package org.grails.cxf.utils

import org.codehaus.groovy.grails.commons.GrailsApplication
import org.grails.cxf.artefact.EndpointArtefactHandler

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
        CxfConfigHandler.instance.cxfConfig
    }

    static NavigableConfiguration getCxfNavConfig() {
        new NavigableConfiguration(cxfConfig)
    }

    static Object getConfig(String configPath) {
        cxfNavConfig.get(configPath)
    }

    static void reloadCxfConfig() {
        CxfConfigHandler.instance.reloadCxfConfig()
    }

    static Integer getLoadOnStartup() {
        getConfig(CFG_LOAD_ON_STARTUP) as Integer
    }

    static Map<String, String> getServletsMappings() {
        Object mappings = getConfig(CFG_SERVLET_MAPPINGS)
        assert mappings, 'There must be at least one configured servlet.'
        mappings as Map<String, String>
    }

    static String getDefaultServletName() {
        Object defaultName = getConfig(CFG_DEFAULT_SERVLET)

        if(defaultName instanceof String && !defaultName.isEmpty() && servletsMappings.containsKey(defaultName)) {
            return defaultName
        }

        new TreeMap<String, String>(servletsMappings).firstKey()
    }

    static Boolean getDefaultSoap12Binding() {
        getConfig(CFG_ENDPOINT_SOAP12) as Boolean
    }

    /**
     * Endpoints in /endpoints dir and Services in /services dir
     * @return list of artefacts to wire up
     */
    static List configuredArtefacts() {
        [EndpointArtefactHandler]
    }

    static String flexibleEnumName(String name) {
        name.replaceAll(/(\s|\-)/, '_').toUpperCase()
    }
}
