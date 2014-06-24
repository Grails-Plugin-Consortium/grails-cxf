package org.grails.cxf.utils

import grails.util.Holders
import grails.util.Environment
import groovy.transform.Synchronized
import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory

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
    ConfigObject getCxfConfig() {
        if(config == null) {
            reloadCxfConfig()
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
    void reloadCxfConfig() {
        config = mergeConfig(definedConfig, DEFAULT_CXF_CONFIG_CLASS)
        definedConfig = this.config
    }

    private ConfigObject getDefinedConfig() {
        def configObject = new NavigableConfiguration(Holders.config)
        try {
            return (ConfigObject) configObject.get(CONFIG_PATH)
        } catch(NullPointerException npe) {
            log.error npe
            log.info "config node ${CONFIG_PATH} not found"
            return Holders.config
        }
    }

    private void setDefinedConfig(final ConfigObject c) {
        new NavigableConfiguration(Holders.config).set(CONFIG_PATH, c)
    }

    /**
     * Merge in a secondary config (provided by defaults) into the main config.
     *
     * @param currentConfig the current configuration
     * @param className the name of the config class to load
     */
    private ConfigObject mergeConfig(final ConfigObject currentConfig, final String className) {
        ClassLoader cl = CxfConfigHandler.classLoader
        ConfigSlurper slurper = new ConfigSlurper(Environment.current.name)
        ConfigObject secondaryConfig

        try {
            secondaryConfig = slurper.parse(cl.loadClass(className))
        } catch(ClassNotFoundException e) {
            throw new RuntimeException(e)
        }

        ConfigObject defaultCxfWsConfig = secondaryConfig.cxf
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
    private ConfigObject mergeConfig(final ConfigObject currentConfig, final ConfigObject secondary) {
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
        if(instance) {
            return instance
        }

        synchronized(CxfConfigHandler) {
            if(instance) {
                return instance
            }

            instance = new CxfConfigHandler()
            return instance
        }
    }
}
