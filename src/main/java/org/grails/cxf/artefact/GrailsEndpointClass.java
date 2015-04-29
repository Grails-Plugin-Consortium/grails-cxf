package org.grails.cxf.artefact;

import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public interface GrailsEndpointClass {

    String PROP_EXPOSE = "expose";
    String PROP_ADDRESS = "address";
    String PROP_EXCLUDES = "excludes";
    String PROP_SERVLET_NAME = "servletName";
    String PROP_WSDL = "wsdl";
    String PROP_SOAP12 = "soap12";

    Set<String> DEFAULT_GROOVY_EXCLUDES =
            Collections.unmodifiableSet(
                    new HashSet<String>(Arrays.asList(
                            // Standard list of GroovyObject methods.
                            "getMetaClass",
                            "setMetaClass",
                            "getProperty",
                            "setProperty",
                            "invokeMethod",
                            "getClass",
                            "setClass",
                            "isTransactional",
                            "getTransactional",
                            "setTransactional",
                            "getMetaMethods",
                            "setMetaMethods",
                            "getErrors",
                            "setErrors"
                    )));

    /**
     * The Endpoint Exposure Type represents how we should configure CXF expose the endpoint. This will be used
     * at startup time either by one of the CXF ServerFactoryBeans or the plugins doWithSpring descriptor.
     *
     * @return the EndpointExposureType for this endpoint.
     */
    EndpointExposureType getExpose();

    /**
     * The plugin will attempt to expose every public method intelligently in the endpoint class. We will filter out
     * the default GroovyObject methods and any of the property setters and getters.
     * <p/>
     * This will be used to set the ignoredMethods property on the Cxf ServiceFactoryBean of the Cxf ServerFactoryBean.
     *
     * @return a set of method names to exclude from endpoint exposure.
     */
    Set<String> getExcludes();

    /**
     * Since the plugin allows us to configure and use multiple CXF servlets, this property allows us to choose which
     * servlet to use.
     *
     * @return the name of the servlet to use for this endpoint.
     */
    String getServletName();

    /**
     * The address past the configured servlet path that this endpoint should be exposed at.
     * <p/>
     * This will be used to set the the address property on the Cxf ServerFactoryBean ({@see AbstractEndpointFactory}).
     *
     * @return the address to expose this endpoint at
     */
    String getAddress();

    /**
     * A WSDL can be specified to be used.
     * <p/>
     * By specifying a WSDL like this, it causes the ReflectionServiceFactoryBean populateFromClass property to be set
     * to false.
     *
     * @return a URL to a WDSL that is on the classpath. Putting the WSDL in the src/java path is the best bet.
     */
    URL getWsdl();

    /**
     * Specifies to the EndpointBeanConfiguration class if it should set a binding id of SOAP 1.2 for the server
     * factory that is getting configured.
     *
     * @return if the Cxf Service should generate and use a Soap 1.2 binding.
     */
    Boolean isSoap12();

}
