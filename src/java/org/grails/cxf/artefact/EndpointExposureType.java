package org.grails.cxf.artefact;

import org.apache.cxf.endpoint.AbstractEndpointFactory;
import org.grails.cxf.frontend.GrailsJaxRsServerFactoryBean;
import org.grails.cxf.frontend.GrailsJaxWsServerFactoryBean;
import org.grails.cxf.frontend.GrailsServerFactoryBean;

public enum EndpointExposureType {

    CXF(GrailsServerFactoryBean.class),
    JAXWS(GrailsJaxWsServerFactoryBean.class),
    JAXRS(GrailsJaxRsServerFactoryBean.class);

    Class<? extends AbstractEndpointFactory> factoryBean;

    EndpointExposureType(final Class<? extends AbstractEndpointFactory> factoryBean) {
        this.factoryBean = factoryBean;
    }
}