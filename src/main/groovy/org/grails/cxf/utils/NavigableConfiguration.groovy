package org.grails.cxf.utils

import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory

/**
 * Lets us get and set configuration on {@code ConfigObject} instances by using a string path rather than
 * programmatically navigating thought it.
 * <p>
 * Example:
 * <code>
 *     config = ConfigHolder.config
 *     new NavigableConfiguration(config).get('nested.configuration.no_problem');
 * </code>
 * <p>
 * TODO see if this could be replaced by something like the following
 * Object constraints = Eval.x(co, "x?.grails?.gorm?.default?.constraints");
 */
class NavigableConfiguration {

    private ConfigObject configObject
    private static final Log log = LogFactory.getLog(NavigableConfiguration)

    NavigableConfiguration(final ConfigObject configObject) {
        this.configObject = configObject
    }

    /**
     * Navigate to a configured path on the config object by a '.' delimited string.
     *
     * @param path to navigate to.
     * @return the {@code ConfigObject} or configuration at the specific configuration path.
     */
    Object get(String path) {
        path.tokenize('.').inject(configObject) {ConfigObject node, String key ->
            node."$key"
        }
    }

    /**
     * Copied from the Grails Platform Plugin by Mark Palmer.
     * <p>
     * Set a value on the config object at a path specified by a '.' delimited string.
     * http://github.com/Grailsrocks/grails-platform-core/blob/master/src/groovy/org/grails/plugin/platform/config/PluginConfigurationImpl.groovy
     *
     * @param fullPath of the configuration to set.
     * @param value to set at the specified path.
     * @param overwriteExisting if we should set a value if there already is one.
     */
    void set(String fullPath, Object value, boolean overwriteExisting = true) {
        def config = this.configObject
        def path = fullPath.tokenize('.')
        def valueName
        if(path.size() > 1) {
            valueName = path[-1]
            path = path[0..(path.size() - 2)]
        } else {
            valueName = path[0]
            path = []
        }

        log.debug "Config path is $path , value name is $valueName"

        // Find the last existing element
        path.find { k ->
            // Find the nearest end point in the config
            def c = config[k]
            if(c instanceof ConfigObject) {
                config = c
                return false
            }
            // We should throw here, its an error...
            return true
        }

        if(overwriteExisting || (config instanceof ConfigObject)) {
            config.putAll([(valueName): value])
        }
    }

    String toString() {
        super.toString() + configObject.flatten()
    }
}
