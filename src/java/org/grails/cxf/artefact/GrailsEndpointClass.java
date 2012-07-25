package org.grails.cxf.artefact;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public interface GrailsEndpointClass {

    public static final String EXPOSE_AS = "exposeAs";

    public static final String EXCLUDES = "excludes";

    public static final String SERVLET_NAME = "servletName";

    public static final Set<String> DEFAULT_GROOVY_EXCLUDES =
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
    EndpointExposureType getExposeAs();

    /**
     * The plugin will attempt to expose every public method intelligently in the endpoint class. We will filter out
     * the default GroovyObject methods and any of the property setters and getters.
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
     *
     * @return the address to expose this endpoint at
     */
    String getAddress();
}
