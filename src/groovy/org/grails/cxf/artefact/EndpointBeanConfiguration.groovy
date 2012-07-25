package org.grails.cxf.artefact

import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory
import org.codehaus.groovy.grails.commons.GrailsApplication

/**
 * Various spring DSL definitions for the Cxf Endpoints.
 */
class EndpointBeanConfiguration {

    GrailsApplication grailsApplication

    @Delegate private final Log log = LogFactory.getLog(getClass())

    EndpointBeanConfiguration(final GrailsApplication grailsApplication) {
        this.grailsApplication = grailsApplication
    }

    /**
     * The {@code cxf} bus bean to be used as well as any other supporting beans.
     *
     * @return spring dsl for cxf beans.
     */
    Closure cxfBeans() {
        return {
            cxf(org.apache.cxf.bus.spring.SpringBus)
            debug "Cxf Bus wired."
        }
    }

    /**
     * All of the endpoint artefacts should be autowired singletons just like services are.
     *
     * @return spring dsl for the endpoint artefacts.
     */
    Closure endpointBeans() {
        return {
            eachEndpointArtefact {DefaultGrailsEndpointClass endpointArtefact ->
                String endpointName = endpointArtefact.propertyName
                Class endpointClass = endpointArtefact.clazz

                "${endpointName}"(endpointClass, AUTOWIRED_SINGLETON)
                debug "Endpoint [${endpointArtefact.fullName}] wired as autowired singleton."
            }
        }
    }

    /**
     * Each of the endpoint artefacts get their own Cxf factory bean to create Cxf service endpoints.
     *
     * @return spring dsl for the endpoint service factories.
     */
    Closure factoryBeans() {
        return {
            eachEndpointArtefact {DefaultGrailsEndpointClass endpointArtefact ->
                String endpointName = endpointArtefact.propertyName
                Class endpointClass = endpointArtefact.clazz
                String endpointAddress = endpointArtefact.address
                Class endpointFactoryClass = endpointArtefact.exposeAs.factoryBean
                Set endpointExclusions = endpointArtefact.excludes

                "${endpointName}Factory"(endpointFactoryClass) {
                    address = endpointAddress
                    serviceClass = endpointClass
                    serviceBean = ref(endpointName)
                    ignoredMethods = endpointExclusions
                }
                debug "Cxf endpoint service factory wired for [${endpointArtefact.fullName}] of type [${endpointFactoryClass.simpleName}]."
            }
        }
    }

    /**
     * Each of the endpoints get their own Cxf service bean created by the Cxf factory beans. These beans are specific
     * to a particular servlet.
     *
     * @param servletName configured for the endpoint artefacts.
     * @return spring dsl for the endpoint service beans.
     */
    Closure cxfServiceEndpointBeans(final String servletName) {
        return {
            eachEndpointArtefact(servletName) {DefaultGrailsEndpointClass endpointArtefact ->
                String endpointName = endpointArtefact.propertyName

                "${endpointName}Bean"((endpointName + 'Factory'): "create")
                debug "Cxf endpoint bean wired for [${endpointArtefact.fullName}] on [${servletName}] servlet."
            }
        }
    }

    private static final AUTOWIRED_SINGLETON = {bean ->
        bean.singleton = true
        bean.autowire = 'byName'
    }

    void eachEndpointArtefact(final Closure forEachGrailsClass) {
        grailsApplication.getArtefacts(EndpointArtefactHandler.TYPE).each(forEachGrailsClass)
    }

    void eachEndpointArtefact(final String servletName, final Closure forEachGrailsClass) {
        eachEndpointArtefact {DefaultGrailsEndpointClass endpointArtefact ->
            if (endpointArtefact.servletName == servletName) {
                forEachGrailsClass(endpointArtefact)
            }
        }
    }

}
