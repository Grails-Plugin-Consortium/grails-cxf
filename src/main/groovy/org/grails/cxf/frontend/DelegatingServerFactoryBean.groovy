package org.grails.cxf.frontend

import java.lang.reflect.Method
import org.apache.cxf.frontend.ServerFactoryBean

/**
 * In general it would be great if either delegate, mixin or category ASTs statically worked but alas, we will
 * have to deal with it for now by having three other {@code ServerFactoryBean} classes for each of the supported
 * types.
 */
class DelegatingServerFactoryBean implements MethodIgnoringServerFactoryBean {

    @Delegate ServerFactoryBean serverFactoryBean

    DelegatingServerFactoryBean(final ServerFactoryBean serverFactoryBean) {
        this.serverFactoryBean = serverFactoryBean
    }

    /**
     * Using the service class and the specified exclusions find all of the methods that the service factory should
     * not expose on the endpoint service.
     *
     * @param exclusions @inheritDoc
     */
    void setIgnoredMethods(final Set<String> exclusions) {
        assertCanSetIgnoredMethods()

        serviceClass.methods.findAll {Method method ->
            method.name in exclusions || method.name.startsWith('super$')
        }.each {Method method ->
            serviceFactory.ignoredMethods.add(method)
        }

        // TODO: Side Effect - Refactor this outa here.
        serviceFactory.ignoredClasses.add('groovy.lang.MetaClass')
    }

    private void assertCanSetIgnoredMethods() {
        assert serviceClass, 'ServiceClass must be set before setting ignoredMethods.'
    }

}
