package test

import grails.plugin.spock.IntegrationSpec
import org.grails.cxf.GrailsCXFServerFactoryBean
import org.grails.cxf.GrailsCXFJaxWsServerFactoryBean

/**
 */
class CxfWiringSpec extends IntegrationSpec {

    def testCxfJaxServiceFactory
    def testCxfServiceFactory
    def testCxfService

    def "ensure test cxf service factory is wired up"() {
        when:
        GrailsCXFServerFactoryBean bean = testCxfServiceFactory

        then:
        bean
        bean.address == '/testCxf'
        bean.start
        bean.serviceBean instanceof TestCxfService
        bean.excludedMethods.size() >= 12
        bean.serviceFactory.ignoredMethods.size() == bean.excludedMethods.size() + 2
        bean.serviceFactory.ignoredMethods.find {it.name == 'sonicScrewdriver'}
    }

    def "ensure test cxfjax service factory is wired up"() {
        when:
        GrailsCXFJaxWsServerFactoryBean bean = testCxfJaxServiceFactory

        then:
        bean
        bean.address == '/testCxfJax'
        bean.start
        bean.serviceBean instanceof TestCxfJaxService
        bean.excludedMethods.size() >= 12
        bean.serviceFactory.ignoredMethods.size() == bean.excludedMethods.size() + 1
        !bean.serviceFactory.ignoredMethods.find {it.name == 'sonicScrewdriver'}
    }

}
