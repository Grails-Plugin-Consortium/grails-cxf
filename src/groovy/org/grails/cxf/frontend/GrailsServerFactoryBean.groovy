package org.grails.cxf.frontend

import org.apache.cxf.frontend.ServerFactoryBean

/**
 * A Cxf @{code ServerFactoryBean} that allows us to set methods that should be excluded from the endpoint.
 */
class GrailsServerFactoryBean extends ServerFactoryBean implements MethodIgnoringServerFactoryBean {

    void setIgnoredMethods(final Set exclusions) {
        new DelegatingServerFactoryBean(this).setIgnoredMethods(exclusions)
    }

}
