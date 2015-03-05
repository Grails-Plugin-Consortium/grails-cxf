package org.grails.cxf.frontend

import org.apache.cxf.jaxrs.JAXRSServerFactoryBean

/**
 * A Cxf @{code ServerFactoryBean} that allows us to set methods that should be excluded from the endpoint.
 *
 * TODO: Figure out why JAX RS doesn't need us to set excluded methods.
 */
class GrailsJaxRsServerFactoryBean extends JAXRSServerFactoryBean implements MethodIgnoringServerFactoryBean {

    void setIgnoredMethods(final Set exclusions) {
        // NOOP
    }

}