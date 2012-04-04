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
 * Rewrite for cxf 2.5.2
 *
 * @author Ryan Crum <ryan.j.crum@gmail.com>
 *     Christian Oestreich <acetrike@gmail.com>
 */
public class GrailsCXFServlet extends CXFNonSpringServlet {

    public void init(ServletConfig servletConfig) {
        super.init(servletConfig)

        ServletContext svCtx = getServletContext();

        ApplicationContext ctx = (ApplicationContext) svCtx
                .getAttribute("interface org.springframework.web.context.WebApplicationContext.ROOT");

        // Spring 2.0
        if (ctx == null) {
            ctx = (ApplicationContext) svCtx
                    .getAttribute("org.springframework.web.context.WebApplicationContext.ROOT");
        }

        loadAdditionalConfig(servletConfig, ctx)
    }

    /**
     * Not sure if we want to override/rewire what the default loadBus is doing since it is trying to load the cxf bean from
     * String configLocation = sc.getInitParameter("config-location") or from "/WEB-INF/cxf-servlet.xml" if that is null
     * We won't have this so the cxf bean will be wired by the plugin at boot time.  This is a change from the way 3.x worked.
     * @param sc The servlet config to use
     */
    @Override
    protected void loadBus(ServletConfig sc) {
        ApplicationContext ctx = ApplicationHolder.application.mainContext
//        String configLocation = sc.getInitParameter("config-location")
//        if(!configLocation) {
//            try {
//                InputStream is = sc.getServletContext().getResourceAsStream("/WEB-INF/cxf-servlet.xml")
//                if(is?.available() > 0) {
//                    is.close()
//                    configLocation = "/WEB-INF/cxf-servlet.xml"
//                }
//            } catch(Exception ex) {
//                log 'config load failure', ex
//            }
//        }
//        if(configLocation) {
//            ctx = ApplicationHolder.application.mainContext
//        }

        setBus(ctx ? ctx.getBean("cxf", Bus.class) : BusFactory.newInstance().createBus())
//        setBus(BusFactory.newInstance().createBus())
    }

    protected void loadAdditionalConfig(ServletConfig servletConfig, ApplicationContext ctx) throws ServletException {
//        ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(servletConfig.getServletContext())
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
