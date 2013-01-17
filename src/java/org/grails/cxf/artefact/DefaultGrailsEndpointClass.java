package org.grails.cxf.artefact;

import groovy.lang.MetaProperty;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.groovy.grails.commons.AbstractInjectableGrailsClass;
import org.codehaus.groovy.grails.commons.GrailsClassUtils;
import org.codehaus.groovy.grails.commons.ServiceArtefactHandler;
import org.grails.cxf.utils.EndpointType;
import org.grails.cxf.utils.GrailsCxfEndpoint;
import org.grails.cxf.utils.GrailsCxfEndpointProperty;
import org.grails.cxf.utils.GrailsCxfUtils;

import javax.xml.transform.TransformerConfigurationException;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.*;

public class DefaultGrailsEndpointClass extends AbstractInjectableGrailsClass implements GrailsEndpointClass {

    private static final String CXFJAX = "cxfjax";
    private static final String CXFRS = "cxfrs";
    private static final String CXF = "cxf";
    protected EndpointExposureType expose;
    protected Set<String> excludes;
    protected String servletName;
    protected URL wsdl;
    protected Boolean soap12;
    protected String address;
    protected Set<String> inInterceptors;
    protected Set<String> outInterceptors;
    protected Set<String> inFaultInterceptors;
    protected Set<String> outFaultInterceptors;
    protected Map<String, Object> properties = new HashMap<String, Object>();

    private static final Log log = LogFactory.getLog(DefaultGrailsEndpointClass.class);

    public DefaultGrailsEndpointClass(Class clazz) throws TransformerConfigurationException {
        super(clazz, EndpointArtefactHandler.TYPE);
        setupExpose();
        buildExclusionSet();
        setupAddress();
        setupServletName();
        findWsdl();
        setupSoap12Binding();
        setupInterceptors();
        setupProperties();
    }

    /**
     * This can be configured in the endpoint class by setting {@code static expose = 'CXF'}. Valid strings in the
     * endpoint class are {@code CXF}, {@code JAXWS}, and {@code JAXRS}. If the {@code expose} property of the
     * class is not specified, then the default of {@code CXF} will be used.
     *
     * @return @inheritDoc
     */
    public EndpointExposureType getExpose() {
        return expose;
    }

    /**
     * Setting the property {@code static excludes = ['myPublicMethod']} on the endpoint class will allow clients to
     * add additional method names to be excluded from exposure.
     * <p/>
     * By default all of the GroovyObject methods and getters and setters for properties will be excluded and setting
     * the excludes property on the endpoint class will add to this set.
     * <p/>
     * TODO: I think this is only relevant to SIMPLE Cxf Frontends.
     *
     * @return @inheritDoc
     */
    public Set<String> getExcludes() {
        return excludes;
    }

    /**
     * Since the plugin allows us to configure and use multiple CXF servlets, this property allows us to choose which
     * servlet to use. The servlet name can be configured by using the property servletName on the endpoint class.
     * <p/>
     * By default the first alphabetically will be used.
     *
     * @return @inheritDoc
     */
    public String getServletName() {
        return servletName;
    }

    /**
     * Gets the address that will be set on the Cxf ServerFactoryBean.
     * <p/>
     * TODO Should also allow overriding and basic configuration?
     *
     * @return @inheritDoc
     */
    public String getAddress() {
        return address;
    }

    /**
     * The URL of the Wsdl that is on the classpath.
     *
     * @return @inheritDoc
     */
    public URL getWsdl() {
        return wsdl;
    }

    public Boolean hasWsdl() {
        return expose == EndpointExposureType.JAX_WS_WSDL && wsdl != null;
    }

    public String getNameNoPostfix() {
        if(getPropertyName().endsWith(ServiceArtefactHandler.TYPE)) {
            return StringUtils.removeEnd(getPropertyName(), ServiceArtefactHandler.TYPE);
        } else if(getPropertyName().endsWith(EndpointArtefactHandler.TYPE)) {
            return StringUtils.removeEnd(getPropertyName(), EndpointArtefactHandler.TYPE);
        }
        return getPropertyName();
    }

    public Boolean isSoap12() {
        return soap12;
    }

    public Set<String> getInInterceptors() {
        return inInterceptors;
    }

    public Set<String> getOutInterceptors() {
        return outInterceptors;
    }

    public Set<String> getInFaultInterceptors() {
        return inFaultInterceptors;
    }

    public Set<String> getOutFaultInterceptors() {
        return outFaultInterceptors;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    protected void setupProperties(){
        GrailsCxfEndpoint annotation = getClazz().getAnnotation(GrailsCxfEndpoint.class);
        if(annotation != null && annotation.properties().length > 0) {
            for(GrailsCxfEndpointProperty prop : annotation.properties()){
                properties.put(prop.name(), prop.value());
            }
            log.debug("Endpoint [" + getFullName() + "] configured to use properties " + properties + ".");
        }
    }

    protected void setupInterceptors() {
        GrailsCxfEndpoint annotation = getClazz().getAnnotation(GrailsCxfEndpoint.class);
        if(annotation != null) {
            inInterceptors = setupInterceptorsViaAnnotation("inInterceptors", annotation.inInterceptors());
            outInterceptors = setupInterceptorsViaAnnotation("outInterceptors", annotation.outInterceptors());
            inFaultInterceptors = setupInterceptorsViaAnnotation("inFaultInterceptors", annotation.inFaultInterceptors());
            outFaultInterceptors = setupInterceptorsViaAnnotation("outFaultInterceptors", annotation.outFaultInterceptors());
        }
    }

    protected Set<String> setupInterceptorsViaAnnotation(String name, String[] customInterceptors) {
        Set<String> addedInterceptors = new HashSet<String>();
        if(customInterceptors.length > 0) {
            Collections.addAll(addedInterceptors, customInterceptors);
            log.debug("Endpoint [" + getFullName() + "] configured to use " + name + " " + addedInterceptors + ".");
        }
        return Collections.unmodifiableSet(addedInterceptors);
    }

    protected void setupAddress() {
        address = "/" + getNameNoPostfix();

        GrailsCxfEndpoint annotation = getClazz().getAnnotation(GrailsCxfEndpoint.class);
        if(annotation != null && !annotation.address().equals("")) {
            setupAddressViaAnnotation(annotation);
        } else {
            setupAddressViaProperty();
        }

        log.debug("Endpoint [" + getFullName() + "] configured to use address [" + address + "].");
    }

    private void setupAddressViaProperty() {
        Object propAddress = getPropertyValue(PROP_ADDRESS);
        if(propAddress != null && propAddress instanceof String) {
            address = ((((String) propAddress).startsWith("/")) ? "" : "/") + ((String) propAddress).replace("#name", getNameNoPostfix());
        }
    }

    private void setupAddressViaAnnotation(GrailsCxfEndpoint annotation) {
        String annotationAddress = annotation.address();
        address = ((annotationAddress.startsWith("/")) ? "" : "/") + annotationAddress.replace("#name", getNameNoPostfix());
    }

    protected void setupExpose() {
        expose = EndpointExposureType.JAX_WS; // Default to the most common type.

        GrailsCxfEndpoint annotation = getClazz().getAnnotation(GrailsCxfEndpoint.class);
        if(annotation != null) {
            setupExposeViaAnnotation(annotation);
        } else {
            setupExposeViaProperty();
        }

        if(expose.equals(EndpointExposureType.SIMPLE)) {
            log.warn("Simple Cxf Frontends are generally not recommended. Find out more: http://cxf.apache.org/docs/simple-frontend.html");
        }

        log.debug("Endpoint [" + getFullName() + "] configured to use [" + expose.name() + "].");
    }

    private void setupExposeViaProperty() {
        Object propExpose = getPropertyValue(PROP_EXPOSE);
        String manualExpose = getConfiguredExpose(propExpose);

        if(manualExpose != null && !manualExpose.equals("")) {
            try {
                expose = EndpointExposureType.forExpose(manualExpose);
            } catch(IllegalArgumentException e) {
                log.error("Unsupported endpoint exposure type [" + manualExpose + "] for endpoint [" + getFullName() + "].  Using default type.");
            }
        }
    }

    private void setupExposeViaAnnotation(GrailsCxfEndpoint annotation) {
        EndpointType exposes = annotation.expose();
        try {
            expose = EndpointExposureType.forExpose(exposes.toString());
        } catch(IllegalArgumentException e) {
            log.error("Unsupported endpoint exposure type [" + exposes.toString() + "] for endpoint [" + getFullName() + "].  Using default type.");
        }
    }

    private String getConfiguredExpose(Object propExpose) {
        String manualExpose = null;

        if(propExpose instanceof EndpointType) {
            manualExpose = ((EndpointType) propExpose).toString();
        } else if(propExpose instanceof String) {
            manualExpose = (String) propExpose;
        } else if(propExpose instanceof List) {
            manualExpose = getListExpose((List) propExpose);
        }
        return manualExpose;
    }

    /**
     * Support legacy static expose = [] style exposure.
     *
     * @param propExpose list of values to check
     * @return string of the service type to wire
     */
    private String getListExpose(List propExpose) {
        String manualExpose = null;
        for(Object prop : ((List) propExpose)) {
            if(prop instanceof String || prop instanceof EndpointType) {
                String stringProp = prop.toString().toLowerCase();

                //legacy variables cxf and cxfjax
                if(CXF.equals(stringProp)) {
                    return EndpointType.SIMPLE.toString();
                } else if(CXFJAX.equals(stringProp)) {
                    return EndpointType.JAX_WS.toString();
                } else if(CXFRS.equals(stringProp)) {
                    return EndpointType.JAX_RS.toString();
                }

                try {
                    EndpointExposureType type = EndpointExposureType.forExpose(stringProp);
                    manualExpose = type.toString();
                    break;
                } catch(IllegalArgumentException e) {
                    log.debug("could not identify type for " + prop);
                }
            }
        }
        return manualExpose;
    }

    protected void buildExclusionSet() {
        final Set<String> groovyExcludes = DEFAULT_GROOVY_EXCLUDES;
        GrailsCxfEndpoint annotation = getClazz().getAnnotation(GrailsCxfEndpoint.class);
        Set<String> aggExcludes = new HashSet<String>();

        List<String> automaticExcludes = buildAutomaticExcludes();

        aggExcludes.addAll(groovyExcludes);
        aggExcludes.addAll(automaticExcludes);

        if(annotation != null && annotation.excludes().length > 0) {
            buildExclusionSetFromAnnotation(aggExcludes, annotation);
        } else {
            buildExclusionSetFromProperty(aggExcludes);
        }

        excludes = Collections.unmodifiableSet(aggExcludes);

        log.debug("Endpoint [" + getFullName() + "] configured to exclude methods " + excludes + ".");
    }

    private List<String> buildAutomaticExcludes() {
        List<String> automaticExcludes = new ArrayList<String>();
        for(MetaProperty prop : getMetaClass().getProperties()) {
            int modifiers = prop.getModifiers();
            if(Modifier.isPublic(modifiers) && !Modifier.isStatic(modifiers)) {
                automaticExcludes.add(GrailsClassUtils.getGetterName(prop.getName()));
                automaticExcludes.add(GrailsClassUtils.getSetterName(prop.getName()));
            }
        }
        return automaticExcludes;
    }

    private void buildExclusionSetFromAnnotation(Set<String> aggExcludes, GrailsCxfEndpoint annotation) {
        Collections.addAll(aggExcludes, annotation.excludes());
    }

    private void buildExclusionSetFromProperty(Set<String> aggExcludes) {
        // Get the the methods that are specified for manual exclusion.
        Collection<String> manualExcludes = (Collection<String>) getPropertyOrStaticPropertyOrFieldValue(PROP_EXCLUDES, Collection.class);

        if(manualExcludes != null && !manualExcludes.isEmpty()) {
            manualExcludes.remove("");
            aggExcludes.addAll(manualExcludes);
        }
    }

    protected void setupServletName() {
        String manualServletName = (String) getPropertyOrStaticPropertyOrFieldValue(PROP_SERVLET_NAME, String.class);

        if(manualServletName != null && !manualServletName.equals("") && GrailsCxfUtils.getServletsMappings().containsKey(manualServletName)) {
            servletName = manualServletName;
        } else {
            servletName = GrailsCxfUtils.getDefaultServletName();
        }

        log.debug("Endpoint [" + getFullName() + "] configured to servlet [" + servletName + "].");
    }

    protected void findWsdl() {
        String wsdlLocation = null;
        GrailsCxfEndpoint annotation = getClazz().getAnnotation(GrailsCxfEndpoint.class);

        if(annotation != null && !annotation.wsdl().equals("")) {
            wsdlLocation = annotation.wsdl();
        } else {
            wsdlLocation = (String) getPropertyOrStaticPropertyOrFieldValue(PROP_WSDL, String.class);
        }

        if(wsdlLocation != null && !wsdlLocation.equals("")) {
            wsdl = getClass().getClassLoader().getResource(wsdlLocation);
            if(wsdl == null) {
                log.error("Endpoint [" + getFullName() + "] as WSDL at [" + wsdlLocation + "] but it couldn't be found. Will try to generate the Cxf Service from the endpoint class.");
            }
        }

        log.debug("Endpoint [" + getFullName() + "] configured to use WSDL [" + wsdl + "].");
    }

    protected void setupSoap12Binding() {
        soap12 = GrailsCxfUtils.getDefaultSoap12Binding();

        GrailsCxfEndpoint annotation = getClazz().getAnnotation(GrailsCxfEndpoint.class);

        if(annotation != null) {
            setupSoap12BindingViaAnnotation(annotation);
        } else {
            setupSoap12BindingViaProperty();
        }

        log.debug("Endpoint [" + getFullName() + "] configured to use soap 1.2 [" + soap12 + "].");
    }

    private void setupSoap12BindingViaProperty() {
        Boolean soap12setting = (Boolean) getPropertyOrStaticPropertyOrFieldValue(PROP_SOAP12, Boolean.class);
        if(soap12setting != null) {
            soap12 = soap12setting;
        }
    }

    private void setupSoap12BindingViaAnnotation(GrailsCxfEndpoint annotation) {
        soap12 = annotation.soap12();
    }
}
