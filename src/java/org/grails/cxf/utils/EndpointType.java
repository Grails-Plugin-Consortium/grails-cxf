package org.grails.cxf.utils;

import org.apache.cxf.endpoint.AbstractEndpointFactory;

/**
 */
public enum EndpointType {

        SIMPLE("SIMPLE"),
        JAX_WS("JAX_WS"),
        JAX_WS_WSDL("JAX_WS_WSDL"),
        JAX_RS("JAX_RS");

    String endpointType;

    EndpointType(final String endpointType) {
        this.endpointType = endpointType;
    }

    static EndpointType forExposeAs(String exposeAs) throws IllegalArgumentException {
        String name = GrailsCxfUtils.flexibleEnumName(exposeAs);
        try {
            return EndpointType.valueOf(name);
        } catch(Exception e) {
           throw new IllegalArgumentException(e);
        }
    }
}
