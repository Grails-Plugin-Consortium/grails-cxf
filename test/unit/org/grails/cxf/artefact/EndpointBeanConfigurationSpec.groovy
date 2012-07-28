package org.grails.cxf.artefact

import grails.plugin.spock.UnitSpec
import grails.spring.BeanBuilder
import org.apache.cxf.bus.spring.SpringBus
import org.codehaus.groovy.grails.commons.ConfigurationHolder
import org.codehaus.groovy.grails.commons.GrailsApplication
import org.codehaus.groovy.grails.commons.GrailsClass
import org.grails.cxf.frontend.GrailsJaxWsServerFactoryBean
import org.springframework.beans.MutablePropertyValues
import org.springframework.beans.factory.config.BeanDefinition
import org.springframework.beans.factory.config.RuntimeBeanReference

class EndpointBeanConfigurationSpec extends UnitSpec {

    private class TestOneEndpoint {}

    private class TestTwoEndpoint {}

    private Boolean configWasntSet

    Closure getArtefactsMock = {String type ->
        assert EndpointArtefactHandler.TYPE == type
        return [new DefaultGrailsEndpointClass(TestOneEndpoint),
                new DefaultGrailsEndpointClass(TestTwoEndpoint)] as GrailsClass[]
    }

    GrailsApplication grailsApplicationMock = [getArtefacts: getArtefactsMock] as GrailsApplication

    EndpointBeanConfiguration bc = new EndpointBeanConfiguration(grailsApplicationMock)

    BeanBuilder bb

    def setup() {
        configWasntSet = false
        if(ConfigurationHolder.config == null) {
            ConfigurationHolder.config = new ConfigObject()
            configWasntSet = true
        }

        bb = new BeanBuilder()
    }

    def cleanup() {
        if(configWasntSet) {
            ConfigurationHolder.config = null
        }
    }

    def "cxfBeans sets up the cxf bus bean"() {
        when:
            Map<String, BeanDefinition> beans =
                bb.beans(bc.cxfBeans()).beanDefinitions

        then:
            BeanDefinition cxf = beans.cxf
            cxf.getBeanClassName() == SpringBus.name
            cxf.isSingleton()
            cxf.isAutowireCandidate()
    }

    def "endpointBeans wires all of the endpoint artefacts"() {
        when:
            Map<String, BeanDefinition> beans =
                bb.beans(bc.endpointBeans()).beanDefinitions

        then:
            BeanDefinition testOneEndpoint = beans.testOneEndpoint
            testOneEndpoint.getBeanClassName() == TestOneEndpoint.name
            testOneEndpoint.isSingleton()
            testOneEndpoint.isAutowireCandidate()

            BeanDefinition testTwoEndpoint = beans.testTwoEndpoint
            testTwoEndpoint.getBeanClassName() == TestTwoEndpoint.name
            testTwoEndpoint.isSingleton()
            testTwoEndpoint.isAutowireCandidate()
    }

    def "factoryBeans wires up cxf factories for each of the endpoints"() {
        when:
            Map<String, BeanDefinition> beans =
                bb.beans(bc.factoryBeans()).beanDefinitions

        then:
            BeanDefinition testOneEndpointFactory = beans.testOneEndpointFactory
            testOneEndpointFactory.getBeanClassName() == GrailsJaxWsServerFactoryBean.name
            testOneEndpointFactory.isSingleton()
            testOneEndpointFactory.isAutowireCandidate()

            MutablePropertyValues testOneEFBeanProps = testOneEndpointFactory.getPropertyValues()
            testOneEFBeanProps.getPropertyValue('address').value == '/testOne'
            testOneEFBeanProps.getPropertyValue('serviceClass').value == TestOneEndpoint
            testOneEFBeanProps.getPropertyValue('serviceBean').value == new RuntimeBeanReference('testOneEndpoint')
            testOneEFBeanProps.getPropertyValue('ignoredMethods').value == GrailsEndpointClass.DEFAULT_GROOVY_EXCLUDES

            BeanDefinition testTwoEndpointFactory = beans.testTwoEndpointFactory
            testTwoEndpointFactory.getBeanClassName() == GrailsJaxWsServerFactoryBean.name
            testTwoEndpointFactory.isSingleton()
            testTwoEndpointFactory.isAutowireCandidate()
    
            MutablePropertyValues testTwoEFBeanProps = testTwoEndpointFactory.getPropertyValues()
            testTwoEFBeanProps.getPropertyValue('address').value == '/testTwo'
            testTwoEFBeanProps.getPropertyValue('serviceClass').value == TestTwoEndpoint
            testTwoEFBeanProps.getPropertyValue('serviceBean').value == new RuntimeBeanReference('testTwoEndpoint')
            testTwoEFBeanProps.getPropertyValue('ignoredMethods').value == GrailsEndpointClass.DEFAULT_GROOVY_EXCLUDES
    }

    def "cxfServiceEndpointBeans wires cxf service beans from the the factory"() {
        when:
            Map<String, BeanDefinition> beans =
                bb.beans(bc.cxfServiceEndpointBeans('CXFServlet')).beanDefinitions
        
        then:
            BeanDefinition testOneBean = beans.testOneEndpointBean
            testOneBean.factoryBeanName == 'testOneEndpointFactory'
            testOneBean.factoryMethodName == 'create'

            BeanDefinition testTwoBean = beans.testTwoEndpointBean
            testTwoBean.factoryBeanName == 'testTwoEndpointFactory'
            testTwoBean.factoryMethodName == 'create'
    }

}