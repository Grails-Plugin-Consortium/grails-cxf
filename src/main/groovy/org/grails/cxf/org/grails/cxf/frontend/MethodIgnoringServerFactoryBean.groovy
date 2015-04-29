package org.grails.cxf.frontend

/**
 * A {@code ServerFactoryBean} that allows setting of a list of ignored methods that will be ignored when the
 * service endpoint bean is created.
 */
interface MethodIgnoringServerFactoryBean {

    /**
     * Set the methods that will be ignored on the service class when creating the endpoint.
     *
     * @param exclusions that we don't want to expose on the endpoint.
     */
    void setIgnoredMethods(Set<String> exclusions)
}