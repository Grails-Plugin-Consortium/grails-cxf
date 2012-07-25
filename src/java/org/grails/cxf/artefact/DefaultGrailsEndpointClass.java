package org.grails.cxf.artefact;

import groovy.lang.MetaProperty;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.groovy.grails.commons.AbstractInjectableGrailsClass;
import org.codehaus.groovy.grails.commons.GrailsClassUtils;
import org.grails.cxf.utils.GrailsCxfUtils;

import javax.xml.transform.TransformerConfigurationException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DefaultGrailsEndpointClass extends AbstractInjectableGrailsClass implements GrailsEndpointClass {

    protected EndpointExposureType exposeAs;
    protected Set<String> excludes;
    protected String servletName;

    private static final Log log = LogFactory.getLog(DefaultGrailsEndpointClass.class);

    public DefaultGrailsEndpointClass(Class clazz) throws TransformerConfigurationException {
        super(clazz, EndpointArtefactHandler.TYPE);
        setupExposeAs();
        buildExclusionSet();
        setupServletName();
    }

    /**
     * This can be configured in the endpoint class by setting {@code static exposeAs = 'CXF'}. Valid strings in the
     * endpoint class are {@code CXF}, {@code JAXWS}, and {@code JAXRS}. If the {@code exposeAs} property of the
     * class is not specified, then the default of {@code CXF} will be used.
     *
     * @return @inheritDoc
     */
    public EndpointExposureType getExposeAs() {
        return exposeAs;
    }

    /**
     * Setting the property {@code static excludes = ['myPublicMethod']} on the endpoint class will allow clients to
     * add additional method names to be excluded from exposure.
     *
     * By default all of the GroovyObject methods and getters and setters for properties will be excluded and setting
     * the excludes property on the endpoint class will add to this set.
     *
     * @return @inheritDoc
     */
    public Set<String> getExcludes() {
        return excludes;
    }

    /**
     * Since the plugin allows us to configure and use multiple CXF servlets, this property allows us to choose which
     * servlet to use. The servlet name can be configured by using the property servletName on the endpoint class.
     *
     * By default the first entry in the servlets configuration will be used.
     *
     * @return @inheritDoc
     */
    public String getServletName() {
        return servletName;
    }

    /**
     * TODO should also allow overriding and basic configuration
     *
     * @return @inheritDoc
     */
    public String getAddress() {
        return "/" + getNameNoPostfix();
    }

    public String getNameNoPostfix() {
        return StringUtils.removeEnd(getPropertyName(), EndpointArtefactHandler.TYPE);
    }

    protected void setupExposeAs() {
        exposeAs = EndpointExposureType.CXF;

        String manualExposeAs = (String) getPropertyOrStaticPropertyOrFieldValue(EXPOSE_AS, String.class);
        if (manualExposeAs != null && !manualExposeAs.equals("")) {
            try {
                exposeAs = EndpointExposureType.valueOf(manualExposeAs.toUpperCase());
            } catch (IllegalArgumentException e) {
                log.error("Unsupported endpoint exposure type [" + manualExposeAs + "] for endpoint [" + getFullName() + "]. Using default type.");
            }
        }

        log.debug("Endpoint [" + getFullName() + "] configured to use [" + exposeAs.name() + "].");
    }

    protected void buildExclusionSet() {
        final Set<String> groovyExcludes = DEFAULT_GROOVY_EXCLUDES;

        List<String> automaticExcludes = new ArrayList<String>();
        for (MetaProperty prop : getMetaClass().getProperties()) {
            int modifiers = prop.getModifiers();
            if (Modifier.isPublic(modifiers) && !Modifier.isStatic(modifiers)) {
                automaticExcludes.add(GrailsClassUtils.getGetterName(prop.getName()));
                automaticExcludes.add(GrailsClassUtils.getSetterName(prop.getName()));
            }
        }

        // Get the the methods that are specified for manual exclusion.
        Collection<String> manualExcludes = (Collection<String>) getPropertyOrStaticPropertyOrFieldValue(EXCLUDES, Collection.class);

        Set<String> aggExcludes = new HashSet<String>();
        aggExcludes.addAll(groovyExcludes);
        aggExcludes.addAll(automaticExcludes);
        if (manualExcludes != null && !manualExcludes.isEmpty()) {
            manualExcludes.remove("");
            aggExcludes.addAll(manualExcludes);
        }

        excludes = Collections.unmodifiableSet(aggExcludes);

        log.debug("Endpoint [" + getFullName() + "] configured to exclude methods " + excludes + ".");
    }

    protected void setupServletName() {
        String manualServletName = (String) getPropertyOrStaticPropertyOrFieldValue(SERVLET_NAME, String.class);

        if (manualServletName != null && !manualServletName.equals("") &&
                GrailsCxfUtils.getServletsMappings().containsKey(manualServletName)) {
            servletName = manualServletName;
        } else {
            servletName = GrailsCxfUtils.getDefaultServletName();
        }

        log.debug("Endpoint [" + getFullName() + "] configured to servlet [" + servletName + "].");
    }

}