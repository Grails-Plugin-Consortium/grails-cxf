package org.grails.cxf

import grails.spring.BeanBuilder

import org.apache.cxf.transport.servlet.CXFServlet
import org.springframework.context.ApplicationContext
import org.springframework.context.support.GenericApplicationContext

import javax.servlet.ServletConfig
import javax.servlet.ServletException
import javax.servlet.ServletContext
import org.apache.cxf.BusFactory
import org.apache.cxf.Bus
import org.codehaus.groovy.grails.commons.ConfigurationHolder
import org.codehaus.groovy.grails.commons.ApplicationHolder
import org.apache.cxf.transport.servlet.CXFNonSpringServlet

/**
 * A servlet that instantiates grails services as CXF services through beans after
 * all of the necessary auto/rewiring in CXF has been completed.
 * This is necessary because CXF won't hook our beans up if we only did it in the doWithSpring
 * closure.
 *
 * Rewrite for cxf 2.5.2.  Move away from the CXFServlet as that will attempt to wire from hard coded config file location.
 * We will take care of wiring up the cxf servlet in the plugin bootstrap and wire up our services here as before.
 *
 * @author Ryan Crum <ryan.j.crum@gmail.com>
 *     Christian Oestreich <acetrike@gmail.com>
 */
public class GrailsCXFServlet extends CXFNonSpringServlet {

    public void init(ServletConfig servletConfig) {
        super.init(servletConfig)

        ServletContext svCtx = servletContext

        //todo: there may be a better way to do this now in 2.5.x
        ApplicationContext ctx = (ApplicationContext) svCtx.getAttribute("interface org.springframework.web.context.WebApplicationContext.ROOT")
        // Spring 2.0
        ctx = ctx ?: (ApplicationContext) svCtx.getAttribute("org.springframework.web.context.WebApplicationContext.ROOT")

        loadAdditionalConfig(servletConfig, ctx)
    }

    /**
     * Override/rewire what the default loadBus to wire up our cxf bean instead of default one.
     * If it doesn't exist for some reason we will get a default one (that will probably not work for plugin needs).
     *
     * This is a change from the way 2.3.x worked in which we were using CXFServlet loadBus which is now not
     * the best option after 2.4.x.
     *
     * @param sc The servlet config to use
     */
    @Override
    protected void loadBus(ServletConfig sc) {
        ApplicationContext ctx = ApplicationHolder.application.mainContext
        setBus(ctx ? ctx.getBean("cxf", Bus.class) : BusFactory.newInstance().createBus())
    }

    /**
     * Wire up out service beans here.
     *
     * @param servletConfig
     * @param ctx
     * @throws ServletException
     */
    protected void loadAdditionalConfig(ServletConfig servletConfig, ApplicationContext ctx) throws ServletException {
        def services = GrailsCXFServerFactoryBean.services + GrailsCXFJaxWsServerFactoryBean.services + GrailsCXFRSServerFactoryBean.services
        def childCtx = new GenericApplicationContext(ctx)
        def bb = new BeanBuilder(childCtx)
        bb.beans {
            services.each { service ->
                "${service}Bean"("${service}Factory": "create") {
                }
            }
        }
        childCtx.refresh()
        bb.createApplicationContext()
    }
}
