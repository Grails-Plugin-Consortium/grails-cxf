/**
 * Default configuration values for the plugin.
 */

cxf {
    servlet {
        // Let the GSP Servlet startup first.
        loadDelay = 1

        // Uncomment to change the default servlet when multiple are configured
        // servlet.defaultName = 'CXFServlet'
    }

    servlets = [
        'CXFServlet': '/services/*'
    ]
}

environments {
    test {
        // Get things up and running right away.
        cxf.servlet.loadDelay = 0
    }
}