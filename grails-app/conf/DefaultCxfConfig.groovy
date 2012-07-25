/**
 * Default configuration values for the plugin.
 */

cxf {
    servlet {
        loadDelay = 1

        // Uncomment to change the default servlet when multiple are configured
        // servlet.defaultName = 'CXFServlet'
    }

    servlets = [
        'CXFServlet': '/services/*'
    ]
}