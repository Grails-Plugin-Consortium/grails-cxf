package org.grails.cxf.servlet

import grails.spring.BeanBuilder
import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory
import org.apache.cxf.transport.servlet.CXFServlet
import org.grails.cxf.artefact.EndpointBeanConfiguration
import org.springframework.context.ApplicationContext
import org.springframework.context.support.GenericApplicationContext
import org.springframework.web.context.support.WebApplicationContextUtils

import javax.servlet.ServletConfig

/**
 * A {@code CXFServlet} that loads the service endpoint beans rather than trying to get them from a hard coded
 * spring xml path. The beans are loaded by spring dsl.
 */
class GrailsCxfServlet extends CXFServlet {

    private static final Log log = LogFactory.getLog(GrailsCxfServlet)

    void init(final ServletConfig servletConfig) {
        super.init(servletConfig)
        assertBusConfigured()
        loadAdditionalConfig()
    }

    private void assertBusConfigured() {
        assert bus, 'Cxf Bus wasn\'t found. Things are about to get dicey.'
    }

    /**
     * Wire up our service beans here.
     * TODO: Why here? Why are they not found on the normal application context?
     */
    protected void loadAdditionalConfig() {
        log.debug "Loading additional bean configuration for [${servletName}]."

        ApplicationContext applicationContext = WebApplicationContextUtils.
                getWebApplicationContext(servletContext);

        ApplicationContext childCtx = new GenericApplicationContext(applicationContext)
        childCtx.refresh()
        BeanBuilder bb = new BeanBuilder(childCtx)

        bb.beans {
            EndpointBeanConfiguration beanConfiguration =
                new EndpointBeanConfiguration(applicationContext.grailsApplication)

            with beanConfiguration.cxfServiceEndpointBeans(servletName)
        }

        bb.createApplicationContext()
    }
}
