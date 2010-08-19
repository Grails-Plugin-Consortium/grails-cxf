package org.grails.cxf

import org.apache.cxf.jaxrs.JAXRSServerFactoryBean
import org.apache.cxf.jaxrs.JAXRSServiceFactoryBean
import org.apache.cxf.jaxrs.JAXRSBindingFactory
import org.codehaus.groovy.grails.commons.GrailsClassUtils

/**
 * A bean that creates a CXF REST service and excludes all of the groovy
 * stuff and properties from showing up as web methods.
 *
 * @author Ryan Crum <ryan.j.crum@gmail.com>
 */
class GrailsCXFRSServerFactoryBean extends JAXRSServerFactoryBean {
  static services = []

  public GrailsCXFRSServerFactoryBean(String svcName, Class clz) {
    super()
    services.push(svcName)
    
    bindingId = JAXRSBindingFactory.JAXRS_BINDING_ID;
    resourceClasses = GrailsClassUtils.getStaticPropertyValue(clz, 'resources')
    serviceClass = clz

  }
}
