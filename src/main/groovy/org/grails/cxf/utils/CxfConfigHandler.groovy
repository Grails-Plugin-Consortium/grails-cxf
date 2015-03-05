package org.grails.cxf.utils

import grails.config.Config
import grails.core.GrailsApplication
import grails.util.Environment
import grails.util.Holders
import groovy.transform.Synchronized
import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory
import org.grails.cxf.DefaultCxfConfig

/**
 * Handles the Plugins Configuration
 */
class CxfConfigHandler {

    private static final Log log = LogFactory.getLog(CxfConfigHandler)
    private static final String CONFIG_PATH = 'cxf'
    private static final String DEFAULT_CXF_CONFIG_CLASS = 'DefaultCxfConfig'

    private ConfigObject config

    /**
     * @return The groovy ConfigObject representation of this plugins configuration.
     */
    @Synchronized
    ConfigObject getCxfConfig(GrailsApplication grailsApplication = Holders.grailsApplication) {
        if (config == null) {
            reloadCxfConfig(grailsApplication)
        }

        config
    }

    @Synchronized
    void setCxfConfig(ConfigObject config) {
        this.config = config
    }

    /**
     * Force a reload of the Cxf Plugin configuration.
     */
    void reloadCxfConfig(GrailsApplication grailsApplication) {
        config = mergeConfig(grailsApplication, definedConfig, DEFAULT_CXF_CONFIG_CLASS)
        setDefinedConfig(grailsApplication, this.config)
    }

    private static ConfigObject getDefinedConfig(GrailsApplication grailsApplication = Holders.grailsApplication) {
//        ConfigObject configObject
//        try {
//            configObject = new ConfigSlurper().parse(grailsApplication?.config?.toProperties())
//        } catch (NullPointerException npe) {
//            log.error npe
//            log.info "config node ${CONFIG_PATH} not found"
//            configObject = new ConfigObject()
//        }

        return new ConfigObject()
    }

    private static void setDefinedConfig(GrailsApplication grailsApplication, final ConfigObject c) {
//        def configObject = new ConfigSlurper().parse(grailsApplication?.config?.toProperties())
        def configObject = new ConfigObject()
        new NavigableConfiguration(configObject).set(CONFIG_PATH, c)
    }

    /**
     * Merge in a secondary config (provided by defaults) into the main config.
     *
     * @param currentConfig the current configuration
     * @param className the name of the config class to load
     */
    private static ConfigObject mergeConfig(final GrailsApplication grailsApplication, final ConfigObject currentConfig, final String className) {

//        return currentConfig
        ClassLoader cl = CxfConfigHandler.classLoader
        ConfigSlurper slurper = new ConfigSlurper(Environment.current.name)
        ConfigObject secondaryConfig

        try {
//            secondaryConfig = slurper.parse(cl.loadClass(className))
            secondaryConfig = slurper.parse(DefaultCxfConfig)
        } catch(ClassNotFoundException e) {
            throw new RuntimeException(e)
        }

        ConfigObject defaultCxfWsConfig = secondaryConfig?.cxf
        mergeConfig(currentConfig, defaultCxfWsConfig)
    }

    /**
     * Merge two configs together. The order is important; if <code>secondary</code> is not null then
     * start with that and merge the main config on top of that. This lets the <code>secondary</code>
     * config act as default values but let user-supplied values in the main config override them.
     *
     * @param currentConfig the main config, starting from Config.groovy
     * @param secondary new default values
     * @return the merged configs
     */
    private static ConfigObject mergeConfig(final ConfigObject currentConfig, final ConfigObject secondary) {
        ConfigObject config = new ConfigObject()
        config.putAll(secondary?.merge(currentConfig) ?: currentConfig)
        config
    }

    // Below lies the Singleton implementation
    // In future updates use the @Singleton transformation.

    private static volatile CxfConfigHandler instance

    private CxfConfigHandler() {
        // Singleton
        // Upgrading to Groovy 1.8 will allow us to use the @Singleton AST Transformation
    }

    static CxfConfigHandler getInstance() {
        if (instance) {
            return instance
        }

        synchronized (CxfConfigHandler) {
            if (instance) {
                return instance
            }

            instance = new CxfConfigHandler()
            return instance
        }
    }
}
