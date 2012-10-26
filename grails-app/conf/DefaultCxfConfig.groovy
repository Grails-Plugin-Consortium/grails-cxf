/**
 * Default configuration values for the plugin.  You can override in Config.groovy
 */

cxf {
    servlet {

        /**
         * cxf.servlet.loadOnStartup
         * <p>
         * Specifies the order in which to load the servlet. Lower positive
         * values load first, while negative or unspecified mean that the
         * sevlet can be loaded at anytime.
         */
        loadOnStartup = 10

        /**
         * cxf.servlet.defaultServlet
         * <p>
         * When multiple servlets are defined by the {@code cxf.servlets}
         * configuration value this specifies the default binding for endpoints
         * that don't explicitly define a {@code static servlet = name}. If
         * this value is not set then the first alphabetically will be used.
         */
        //defaultServlet = 'CxfServlet'
    }

    /**
     * cxf.servlets
     * <p>
     * A map of Servlet Name -> Servlet Mapping Pattern. If multiple Cxf
     * servlets are required or a different mapping pattern is needed this
     * configuration allows that.
     */
    servlets = [
            CxfServlet: '/services/*'
    ]

    endpoint {

        /**
         * cxf.endpoint.soap12Binding
         * <p>
         * Sets the Cxf Server Factory to generate a Soap 1.2 binding. This can
         * also be set on a per endpoint basis using
         * {@code static soap12 = true}.
         */
        soap12Binding = false
    }
}


