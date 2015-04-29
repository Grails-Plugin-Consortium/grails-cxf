package org.grails.cxf.artefact;

import org.apache.cxf.endpoint.AbstractEndpointFactory;
import org.grails.cxf.frontend.GrailsJaxRsServerFactoryBean;
import org.grails.cxf.frontend.GrailsJaxWsServerFactoryBean;
import org.grails.cxf.frontend.GrailsSimpleServerFactoryBean;
import org.grails.cxf.utils.GrailsCxfUtils;

public enum EndpointExposureType {

    SIMPLE(GrailsSimpleServerFactoryBean.class),
    JAX_WS(GrailsJaxWsServerFactoryBean.class),
    JAX_WS_WSDL(GrailsJaxWsServerFactoryBean.class),
    JAX_RS(GrailsJaxRsServerFactoryBean.class);

    Class<? extends AbstractEndpointFactory> factoryBean;

    EndpointExposureType(final Class<? extends AbstractEndpointFactory> factoryBean) {
        this.factoryBean = factoryBean;
    }

    static EndpointExposureType forExpose(String expose) throws IllegalArgumentException {
        String name = GrailsCxfUtils.flexibleEnumName(expose);
        try {
            return EndpointExposureType.valueOf(name);
        } catch(Exception e) {
            throw new IllegalArgumentException(e);
            //return EndpointExposureType.valueOf("JAX_WS");
        }
    }
}
