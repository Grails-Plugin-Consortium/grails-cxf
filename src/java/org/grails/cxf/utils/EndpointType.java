package org.grails.cxf.utils;

public enum EndpointType {
        SIMPLE("SIMPLE"),
        JAX_WS("JAX_WS"),
        JAX_WS_WSDL("JAX_WS_WSDL"),
        JAX_RS("JAX_RS");

    String endpointType;

    EndpointType(final String endpointType) {
        this.endpointType = endpointType;
    }

    public static EndpointType forExpose(String expose) throws IllegalArgumentException {
        String name = GrailsCxfUtils.flexibleEnumName(expose);
        try {
            return EndpointType.valueOf(name);
        } catch(Exception e) {
           throw new IllegalArgumentException(e);
        }
    }
}
