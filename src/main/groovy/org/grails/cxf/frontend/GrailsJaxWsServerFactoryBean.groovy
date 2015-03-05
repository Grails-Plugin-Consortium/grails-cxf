package org.grails.cxf.frontend

import org.apache.cxf.jaxws.JaxWsServerFactoryBean

/**
 * A Cxf @{code ServerFactoryBean} that allows us to set methods that should be excluded from the endpoint.
 */
class GrailsJaxWsServerFactoryBean extends JaxWsServerFactoryBean implements MethodIgnoringServerFactoryBean {

    void setIgnoredMethods(final Set exclusions) {
        new DelegatingServerFactoryBean(this).ignoredMethods = exclusions
    }
}
