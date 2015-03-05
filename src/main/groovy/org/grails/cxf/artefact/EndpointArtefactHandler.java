package org.grails.cxf.artefact;


import grails.core.ArtefactHandlerAdapter;
import grails.core.GrailsClass;

public class EndpointArtefactHandler extends ArtefactHandlerAdapter {

    public static final String TYPE = "Endpoint";

    public EndpointArtefactHandler() {
        super(TYPE, GrailsClass.class, DefaultGrailsEndpointClass.class, TYPE);
    }

    @Override
    public boolean isArtefactClass(Class clazz) {
        // class shouldn't be null and should ends with Endpoint suffix
        return (clazz != null && clazz.getName().endsWith(TYPE));
    }
}

