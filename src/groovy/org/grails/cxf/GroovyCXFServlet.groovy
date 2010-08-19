package org.grails.cxf

import org.apache.cxf.transport.servlet.CXFServlet
import org.springframework.context.ApplicationContext
import javax.servlet.ServletConfig
import javax.servlet.ServletException
import grails.spring.BeanBuilder
import javax.servlet.ServletContext
import org.springframework.context.support.GenericApplicationContext
import org.apache.cxf.frontend.ServerFactoryBean

/**
 * A servlet that instantiates grails services as CXF services through beans after
 * all of the necessary auto/rewiring in CXF has been completed.
 * This is necessary because CXF won't hook our beans up if we only did it in the doWithSpring
 * closure.
 *
 * @author Ryan Crum <ryan.j.crum@gmail.com>
 */
public class GrailsCXFServlet extends CXFServlet {
  def grailsApplication

  public void init(ServletConfig servletConfig) {
    super.init(servletConfig)

    // try to pull an existing ApplicationContext out of the
    // ServletContext
    ServletContext svCtx = getServletContext();

    // Spring 1.x
    ApplicationContext ctx = (ApplicationContext) svCtx
        .getAttribute("interface org.springframework.web.context.WebApplicationContext.ROOT");

    // Spring 2.0
    if (ctx == null) {
      ctx = (ApplicationContext) svCtx
          .getAttribute("org.springframework.web.context.WebApplicationContext.ROOT");
    }
    
    loadAdditionalConfig(ctx, servletConfig)
  }
  
  protected void loadAdditionalConfig(ApplicationContext ctx, ServletConfig servletConfig)
      throws ServletException {
    super.loadAdditionalConfig(ctx, servletConfig)
    
    def services = GrailsCXFServerFactoryBean.services + GrailsCXFJaxWsServerFactoryBean.services + GrailsCXFRSServerFactoryBean.services
    def childCtx = new GenericApplicationContext(ctx)
    def bb = new BeanBuilder(childCtx)
    bb.beans {
      services.each { service ->
        "${service}Bean"("${service}Factory":"create") {
        }
      }
    }
    childCtx.refresh()
    bb.createApplicationContext()
  }
}
