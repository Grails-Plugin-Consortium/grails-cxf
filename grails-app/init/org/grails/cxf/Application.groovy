package org.grails.cxf

import grails.boot.GrailsApp
import grails.boot.config.GrailsAutoConfiguration
import org.apache.cxf.Bus
import org.apache.cxf.bus.spring.SpringBus
import org.apache.cxf.transport.servlet.CXFServlet
import org.grails.cxf.test.soap.interceptor.CustomLoggingInInterceptor
import org.grails.cxf.test.soap.interceptor.InjectedBean
import org.springframework.boot.context.embedded.ServletRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.ImportResource

@ImportResource(["classpath:META-INF/cxf/cxf.xml",
        "classpath:META-INF/cxf/cxf-extension-jaxws.xml",
        "classpath:META-INF/cxf/cxf-servlet.xml"])
@ComponentScan(["org.grails.cxf.listener", "org.grails.cxf.test.soap.interceptor"])
class Application extends GrailsAutoConfiguration {

    static void main(String[] args) {
        GrailsApp.run(Application)
    }

    @Bean
    public ServletRegistrationBean cxfServlet() {
        ServletRegistrationBean servlet = new ServletRegistrationBean(new CXFServlet(), "/services/*");
        servlet.setLoadOnStartup(1);
        return servlet;
    }

    @Bean(name = Bus.DEFAULT_BUS_ID)
    public SpringBus springBus() {
        return new SpringBus();
    }

    @Bean
    public CustomLoggingInInterceptor customLoggingInInterceptor(){
        return new CustomLoggingInInterceptor(name: 'CustomLoggingInInterceptor')
    }

    @Bean
    public InjectedBean injectedBean(){
        return new InjectedBean(name: 'InjectedBean')
    }
}