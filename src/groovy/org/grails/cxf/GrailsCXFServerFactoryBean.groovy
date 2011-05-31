package org.grails.cxf

import org.apache.cxf.jaxws.JaxWsServerFactoryBean
import org.apache.cxf.frontend.ServerFactoryBean
import org.apache.cxf.service.factory.ReflectionServiceFactoryBean
import org.codehaus.groovy.grails.commons.GrailsClassUtils

/**
 * A bean that creates a CXF jaxb service and excludes all of the groovy
 * stuff and properties from showing up as web methods.
 *
 * @author Ryan Crum <ryan.j.crum@gmail.com>
 */
class GrailsCXFServerFactoryBean extends ServerFactoryBean {
  static services = []
  def excludedMethods = [
    "getMetaClass",
    "setMetaClass", 
    "getProperty",
    "setProperty",
    "invokeMethod",
    "isTransactional",
    "getTransactional",
    "setTransactional", 
    "getMetaMethods",
    "setMetaMethods",
    "getErrors",
    "setErrors"
  ]

  public GrailsCXFServerFactoryBean(String svcName, Class clz) {
    super()
    services.push(svcName)

    def svcFact = (getServiceFactory() as ReflectionServiceFactoryBean)

    clz.metaClass.properties.each { prop ->
      excludedMethods.push(GrailsClassUtils.getGetterName(prop.name))
      excludedMethods.push(GrailsClassUtils.getSetterName(prop.name))
    }

    def manualExclusions = GrailsClassUtils.getStaticPropertyValue(clz, 'exclude')
    
    clz.methods.each { method ->
      if (method.name in excludedMethods
        || method.name in manualExclusions
        || method.name.startsWith("super\$")) {
        svcFact.ignoredMethods.add(method)
      }
    }

    serviceClass = clz
  }
}
