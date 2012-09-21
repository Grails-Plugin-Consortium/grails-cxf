package org.grails.cxf.utils

import org.codehaus.groovy.grails.commons.GrailsApplication
import org.grails.cxf.artefact.EndpointArtefactHandler
import org.codehaus.groovy.grails.commons.ServiceArtefactHandler

class GrailsCxfUtils {

    static final String CFG_LOAD_ON_STARTUP = 'servlet.loadOnStartup'
    static final String CFG_DEFAULT_SERVLET = 'servlet.defaultServlet'
    static final String CFG_SERVLET_MAPPINGS = 'servlets'
    static final String CFG_ENDPOINT_SOAP12 = 'endpoint.soap12Binding'

    //this is injected from bootstrap... ConfigurationHolder is now depreciated
    static GrailsApplication grailsApplication

    private GrailsCxfUtils() {
        // Class contains static methods only
    }

    static ConfigObject getCxfConfig() {
        CxfConfigHandler.instance.getCxfConfig()
    }

    static NavigableConfiguration getCxfNavConfig() {
        return new NavigableConfiguration(getCxfConfig())
    }

    static Object getConfig(String configPath) {
        getCxfNavConfig().get(configPath)
    }

    static void reloadCxfConfig() {
        CxfConfigHandler.instance.reloadCxfConfig()
    }

    static Integer getLoadOnStartup() {
        return getConfig(CFG_LOAD_ON_STARTUP) as Integer
    }

    static Map<String, String> getServletsMappings() {
        Object mappings = getConfig(CFG_SERVLET_MAPPINGS)
        assert mappings, "There must be at least one configured servlet."
        return mappings as Map<String, String>
    }

    static String getDefaultServletName() {
        Object defaultName = getConfig(CFG_DEFAULT_SERVLET)

        if(defaultName instanceof String && !defaultName.isEmpty() && getServletsMappings().containsKey(defaultName)) {
            return defaultName
        }

        return new TreeMap<String, String>(getServletsMappings()).firstKey()
    }

    static Boolean getDefaultSoap12Binding() {
        return getConfig(CFG_ENDPOINT_SOAP12) as Boolean
    }

    /**
     * Endpoints in /endpoints dir and Services in /services dir
     * @return list of artefacts to wire up
     */
    static List configuredArtefacts() {
        return [EndpointArtefactHandler]
    }

    static String flexibleEnumName(String name) {
        return name.replaceAll(/(\s|\-)/, '_').toUpperCase()
    }
}
