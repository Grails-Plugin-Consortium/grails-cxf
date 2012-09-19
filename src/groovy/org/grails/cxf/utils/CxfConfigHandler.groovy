package org.grails.cxf.utils

import grails.util.Environment
import org.codehaus.groovy.grails.commons.ConfigurationHolder

/**
 * Handles the Plugins Configuration
 */
class CxfConfigHandler {

    private static final String CONFIG_PATH = "grails.plugins.cxf"
    private static final String DEFAULT_CXF_CONFIG_CLASS = "DefaultCxfConfig"

    private ConfigObject cxfConfig

    /**
     * @return The groovy ConfigObject representation of this plugins configuration.
     */
    synchronized ConfigObject getCxfConfig() {
        if (cxfConfig == null) {
            reloadCxfConfig()
        }

        return cxfConfig
    }

    /**
     * Force a reload of the Cxf Plugin configuration.
     */
    void reloadCxfConfig() {
        cxfConfig = mergeConfig(getDefinedConfig(), DEFAULT_CXF_CONFIG_CLASS)
        setDefinedConfig(cxfConfig)
    }

    private ConfigObject getDefinedConfig() {
        return (ConfigObject) new NavigableConfiguration(ConfigurationHolder.config).get(CONFIG_PATH)
    }

    private void setDefinedConfig(final ConfigObject c) {
        new NavigableConfiguration(ConfigurationHolder.config).set(CONFIG_PATH, c)
    }

    /**
     * Merge in a secondary config (provided by defaults) into the main config.
     *
     * @param currentConfig the current configuration
     * @param className the name of the config class to load
     */
    private ConfigObject mergeConfig(final ConfigObject currentConfig, final String className) {
        ClassLoader cl = CxfConfigHandler.getClassLoader()
        ConfigSlurper slurper = new ConfigSlurper(Environment.getCurrent().getName())
        ConfigObject secondaryConfig

        try {
            secondaryConfig = slurper.parse(cl.loadClass(className))
        } catch(ClassNotFoundException e) {
            throw new RuntimeException(e)
        }

        ConfigObject defaultCxfWsConfig = secondaryConfig.cxf
        return mergeConfig(currentConfig, defaultCxfWsConfig)
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
        config.putAll(!secondary ? currentConfig : secondary.merge(currentConfig))
        return config
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
