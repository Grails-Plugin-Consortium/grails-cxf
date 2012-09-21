package org.grails.cxf.artefact

import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory
import org.codehaus.groovy.grails.commons.GrailsApplication
import org.codehaus.groovy.grails.commons.GrailsClassUtils

import javax.xml.ws.soap.SOAPBinding

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
            //wire the endpoints
            eachEndpointArtefact {DefaultGrailsEndpointClass endpointArtefact ->
                String endpointName = endpointArtefact.propertyName
                Class endpointClass = endpointArtefact.clazz
                String endpointAddress = endpointArtefact.address
                Class endpointFactoryClass = endpointArtefact.exposeAs.factoryBean
                Set endpointExclusions = endpointArtefact.excludes
                Boolean endpointUseWsdl = endpointArtefact.hasWsdl()
                String endpointWsdlLocation = endpointArtefact.wsdl?.toString()
                Boolean endpointUseSoap12 = endpointArtefact.soap12

                "${endpointName}Factory"(endpointFactoryClass) {
                    address = endpointAddress
                    serviceClass = endpointClass
                    serviceBean = ref(endpointName)
                    ignoredMethods = endpointExclusions

                    if(endpointUseWsdl) {
                        wsdlLocation = endpointWsdlLocation
                    }

                    if(endpointUseSoap12) {
                        bindingId = SOAPBinding.SOAP12HTTP_MTOM_BINDING
                    }
                }

                debug "Cxf endpoint server factory wired for [${endpointArtefact.fullName}] of type [${endpointFactoryClass.simpleName}]."
                trace 'Cxf endpoint server factory bean wiring details:' +
                      "\n\tEndpoint Name:                       $endpointName" +
                      "\n\tEndpoint Class:                      $endpointClass" +
                      "\n\tServer Factory Class:                $endpointFactoryClass.simpleName" +
                      "\n\tServer Factory - Address:            $endpointAddress" +
                      "\n\tServer Factory - Service Class:      $endpointClass" +
                      "\n\tServer Factory - Service Bean:       ref($endpointName)" +
                      "\n\tServer Factory - Ignored Methods:    $endpointExclusions" +
                      "\n\tServer Factory - Wsdl Defined:       $endpointUseWsdl" +
                      "\n\tServer Factory - Wsdl Location:      $endpointWsdlLocation" +
                      "\n\tServer Factory - Soap 1.2 Binding:   $endpointUseSoap12" +
                      "\n"
            }

            //now wire the services
            eachServiceArtefact {DefaultGrailsEndpointClass endpointArtefact ->
                String endpointName = endpointArtefact.propertyName
                Class endpointClass = endpointArtefact.clazz
                String endpointAddress = endpointArtefact.address
                Class endpointFactoryClass = endpointArtefact.exposeAs.factoryBean
                Set endpointExclusions = endpointArtefact.excludes
                Boolean endpointUseWsdl = endpointArtefact.hasWsdl()
                String endpointWsdlLocation = endpointArtefact.wsdl?.toString()
                Boolean endpointUseSoap12 = endpointArtefact.soap12

                "${endpointName}Factory"(endpointFactoryClass) {
                    address = endpointAddress
                    serviceClass = endpointClass
                    serviceBean = ref(endpointName)
                    ignoredMethods = endpointExclusions

                    if(endpointUseWsdl) {
                        wsdlLocation = endpointWsdlLocation
                    }

                    if(endpointUseSoap12) {
                        bindingId = SOAPBinding.SOAP12HTTP_MTOM_BINDING
                    }
                }

                debug "Cxf endpoint server factory wired for [${endpointArtefact.fullName}] of type [${endpointFactoryClass.simpleName}]."
                trace 'Cxf endpoint server factory bean wiring details:' +
                      "\n\tEndpoint Name:                       $endpointName" +
                      "\n\tEndpoint Class:                      $endpointClass" +
                      "\n\tServer Factory Class:                $endpointFactoryClass.simpleName" +
                      "\n\tServer Factory - Address:            $endpointAddress" +
                      "\n\tServer Factory - Service Class:      $endpointClass" +
                      "\n\tServer Factory - Service Bean:       ref($endpointName)" +
                      "\n\tServer Factory - Ignored Methods:    $endpointExclusions" +
                      "\n\tServer Factory - Wsdl Defined:       $endpointUseWsdl" +
                      "\n\tServer Factory - Wsdl Location:      $endpointWsdlLocation" +
                      "\n\tServer Factory - Soap 1.2 Binding:   $endpointUseSoap12" +
                      "\n"
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

    /**
     * We only want services with explicit 'exposeAs' wired up.
     * @param forEachGrailsClass
     */
    void eachServiceArtefact(final Closure forEachGrailsClass) {
        grailsApplication.serviceClasses.each { service ->
            def exposeAs = GrailsClassUtils.getStaticPropertyValue(service.getClazz(), 'exposeAs')
            if(exposeAs) {
                forEachGrailsClass(new DefaultGrailsEndpointClass(service.clazz))
            }
        }
    }

    void eachEndpointArtefact(final String servletName, final Closure forEachGrailsClass) {
        eachEndpointArtefact {DefaultGrailsEndpointClass endpointArtefact ->
            if(endpointArtefact.servletName == servletName) {
                forEachGrailsClass(endpointArtefact)
            }
        }

        eachServiceArtefact {DefaultGrailsEndpointClass endpointArtefact ->
            if(endpointArtefact.servletName == servletName) {
                forEachGrailsClass(endpointArtefact)
            }
        }
    }
}
