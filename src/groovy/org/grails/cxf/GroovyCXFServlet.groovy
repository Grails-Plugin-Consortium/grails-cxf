package org.grails.cxf

import grails.spring.BeanBuilder
import org.apache.cxf.transport.servlet.CXFServlet
import org.springframework.context.ApplicationContext
import org.springframework.context.support.GenericApplicationContext
import org.springframework.web.context.support.WebApplicationContextUtils

import javax.servlet.ServletConfig
import javax.servlet.ServletException

/**
 * A servlet that instantiates grails services as CXF services through beans after
 * all of the necessary auto/rewiring in CXF has been completed.
 * This is necessary because CXF won't hook our beans up if we only did it in the doWithSpring
 * closure.
 *
 * Rewrite for cxf 2.5.2
 *
 * @author Ryan Crum <ryan.j.crum@gmail.com>
 *     Christian Oestreich <acetrike@gmail.com>
 */
public class GrailsCXFServlet extends CXFServlet {

    public void init(ServletConfig servletConfig) {
        super.init(servletConfig)

        ApplicationContext wac = WebApplicationContextUtils.getWebApplicationContext(servletConfig.getServletContext())
        loadAdditionalConfig(wac, servletConfig)
    }

    /**
     * Not sure if we want to override/rewire what the default loadBus is doing since it is trying to load the cxf bean from
     * String configLocation = sc.getInitParameter("config-location"); or from "/WEB-INF/cxf-servlet.xml"; if that is null
     * We won't have this so the cxf bean will be wired by the plugin at boot time.  This is a change from the way 3.x worked.
     * @param sc The servlet config to use
     */
    @Override
    protected void loadBus(ServletConfig sc) {
        super.loadBus(sc)
    }

    protected void loadAdditionalConfig(ApplicationContext ctx, ServletConfig servletConfig) throws ServletException {

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
