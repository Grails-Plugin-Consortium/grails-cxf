package org.grails.cxf

import org.apache.cxf.jaxws.JaxWsServerFactoryBean
import org.apache.cxf.jaxws.support.JaxWsServiceFactoryBean
import org.codehaus.groovy.grails.commons.GrailsClassUtils

/**
 * A bean that creates a CXF jaxb service and excludes all of the groovy
 * stuff and properties from showing up as web methods.
 *
 * @author Ryan Crum <ryan.j.crum@gmail.com>
 */
class GrailsCXFJaxWsServerFactoryBean extends JaxWsServerFactoryBean {

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

  public GrailsCXFJaxWsServerFactoryBean(String svcName, Class clz) {
    super()
    services.push(svcName)

    def svcFact = (getServiceFactory() as JaxWsServiceFactoryBean)

    clz.metaClass.properties.each { prop ->
      excludedMethods.push(GrailsClassUtils.getGetterName(prop.name))
      excludedMethods.push(GrailsClassUtils.getSetterName(prop.name))
    }

    def manualExclusions = GrailsClassUtils.getStaticPropertyValue(clz, 'exclude')

    clz.methods.each { method ->
      if(        method.name in excludedMethods
              || method.name in manualExclusions
              || method.name.startsWith("super\$")) {
        svcFact.ignoredMethods.add(method)
      }
    }

    serviceClass = clz
  }
}
