package org.grails.cxf.utils

import org.grails.cxf.artefact.EndpointArtefactHandler

/**
 *
 */
class GrailsCxfUtils {

    private GrailsCxfUtils() {
        // Class contains static methods only
    }

    static ConfigObject getCxfConfig() {
        CxfConfigHandler.instance.getCxfConfig()
    }

    static void reloadCxfConfig() {
        CxfConfigHandler.instance.reloadCxfConfig()
    }

    static Integer getLoadDelay() {
        return getCxfConfig().servlet.loadDelay
    }

    static Map<String, String> getServletsMappings() {
        assert getCxfConfig().servlets, "There must be at least one configured servlet."
        return getCxfConfig().servlets
    }

    static String getDefaultServletName() {
        Object defaultName = getCxfConfig().servlet.defaultName

        if(defaultName instanceof String && !defaultName.isEmpty() && getServletsMappings().containsKey(defaultName)) {
            return defaultName
        }

        return new TreeMap<String, String>(getServletsMappings()).firstKey()
    }

    static List configuredArtefacts() {
        return [EndpointArtefactHandler]
    }

    static String flexibleEnumName(String name) {
        return name.replaceAll(/(\s|\-)/, '_').toUpperCase()
    }
}
