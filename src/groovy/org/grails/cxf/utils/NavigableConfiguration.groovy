package org.grails.cxf.utils

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
        path.tokenize('.').inject(configObject) {ConfigObject node ,String key ->
            return node."$key"
        }
    }

    /**
     * Copied from the Grails Platform Plugin by Mark Palmer.
     * <p>
     * Set a value on the config object at a path specified by a '.' delimited string.
     *
     * @param fullPath of the configuration to set.
     * @param value to set at the specified path.
     * @param overwriteExisting if we should set a value if there already is one.
     * @see https://github.com/Grailsrocks/grails-platform-core/blob/master/src/groovy/org/grails/plugin/platform/config/PluginConfigurationImpl.groovy
     */
    void set(String fullPath, Object value, boolean overwriteExisting = true) {
        def configObject = this.configObject
        def path = fullPath.tokenize('.')
        def valueName
        if (path.size() > 1) {
            valueName = path[-1]
            path = path[0..(path.size() - 2)]
        } else {
            valueName = path[0]
            path = []
        }

        // Find the last existing element
        path.find { k ->
            // Find the nearest end point in the config
            def c = configObject[k]
            if (c instanceof ConfigObject) {
                configObject = c
                return false
            } else {
                // We should throw here, its an error...
                return true
            }
        }

        if (overwriteExisting || (configObject instanceof ConfigObject)) {
            configObject.putAll([(valueName): value])
        }
    }
}
