package org.grails.cxf.artefact

import org.codehaus.groovy.grails.commons.GrailsApplication
import org.grails.cxf.MockConfigurationSpec
import org.grails.cxf.utils.CxfConfigHandler
import org.grails.cxf.utils.GrailsCxfUtils
import spock.lang.Unroll

import static org.grails.cxf.artefact.EndpointExposureType.*
import static org.grails.cxf.artefact.GrailsEndpointClass.DEFAULT_GROOVY_EXCLUDES

class DefaultGrailsEndpointClassSpec extends MockConfigurationSpec {

    private class DefaultEndpoint {}

    private class SimpleEndpoint {

        static expose = ""
        static excludes = []
        static servletName = ""
    }

    private class TestEndpoint extends SimpleEndpoint {

        String willBeExcluded

        void notExcluded() {}
    }

    private class TheEndpointFooEndpoint {}

    private class ToastAndButter {}

    def setup() {
        CxfConfigHandler.instance.cxfConfig =
            new ConfigSlurper().parse(getClass().getClassLoader().loadClass('DefaultCxfConfig')).cxf

        // Reset this stuff each go
        SimpleEndpoint.expose = ""
        SimpleEndpoint.excludes = []
        SimpleEndpoint.servletName = ""
    }

    def cleanup() {
        CxfConfigHandler.instance.cxfConfig = null
    }

    def "expose properly configures by default"() {
        when:
        def artefact = new DefaultGrailsEndpointClass(DefaultEndpoint)

        then:
        artefact.expose == JAX_WS
    }

    @Unroll
    def "expose can be configured #expose = #expectedExpose"() {
        when:
        SimpleEndpoint.expose = expose
        def artefact = new DefaultGrailsEndpointClass(SimpleEndpoint)

        then:
        artefact.expose == expectedExpose

        where:
        expose      | expectedExpose
        ''            | JAX_WS
        null          | JAX_WS
        1234          | JAX_WS
        new Object()  | JAX_WS
        'SIMPLE'      | SIMPLE
        'simple'      | SIMPLE
        'cxf'         | JAX_WS
        'foo'         | JAX_WS
        'JAX_RS'      | JAX_RS
        'JAX-RS'      | JAX_RS
        'JAX RS'      | JAX_RS
        'jax rs'      | JAX_RS
        'jax-rs'      | JAX_RS
        'jax_rs'      | JAX_RS
        'JAX_WS'      | JAX_WS
        'JAX-WS'      | JAX_WS
        'JAX WS'      | JAX_WS
        'jax_ws'      | JAX_WS
        'jax-ws'      | JAX_WS
        'jax ws'      | JAX_WS
        'JAX_WS_WSDL' | JAX_WS_WSDL
        'JAX-WS-WSDL' | JAX_WS_WSDL
        'JAX WS WSDL' | JAX_WS_WSDL
        'jax_ws_wsdl' | JAX_WS_WSDL
        'jax-ws-wsdl' | JAX_WS_WSDL
        'jax ws wsdl' | JAX_WS_WSDL
        'jax ws WSDL' | JAX_WS_WSDL
        'jax-ws WSDL' | JAX_WS_WSDL
    }

    def "excludes is built with defaults"() {
        when:
        def artefact = new DefaultGrailsEndpointClass(DefaultEndpoint)

        then:
        artefact.excludes == DEFAULT_GROOVY_EXCLUDES as Set
    }

    @Unroll
    def "excludes can be added to #excludes = #expectedExcludes"() {
        when:
        SimpleEndpoint.excludes = excludes
        def artefact = new DefaultGrailsEndpointClass(SimpleEndpoint)

        then:
        artefact.excludes == expectedExcludes

        where:
        excludes            | expectedExcludes
        ['']                | DEFAULT_GROOVY_EXCLUDES
        null                | DEFAULT_GROOVY_EXCLUDES
        ['invokeMethod']    | DEFAULT_GROOVY_EXCLUDES
        ['newMethod']       | new HashSet(DEFAULT_GROOVY_EXCLUDES) << 'newMethod'
        ['toast', 'butter'] | new HashSet(DEFAULT_GROOVY_EXCLUDES) << 'toast' << 'butter'
    }

    def "excludes public non static property bean methods"() {
        when:
        def artefact = new DefaultGrailsEndpointClass(TestEndpoint)

        then:
        artefact.excludes == new HashSet(DEFAULT_GROOVY_EXCLUDES) << 'setWillBeExcluded' << 'getWillBeExcluded'
    }

    @Unroll
    def "proper postfix removal #endpointClass = #expectedName"() {
        when:
        def artefact = new DefaultGrailsEndpointClass(endpointClass)

        then:
        artefact.getNameNoPostfix() == expectedName

        where:
        endpointClass          | expectedName
        SimpleEndpoint         | 'simple'
        DefaultEndpoint        | 'default'
        TestEndpoint           | 'test'
        TheEndpointFooEndpoint | 'theEndpointFoo'
        ToastAndButter         | 'toastAndButter'
    }

    def "servletName defaults on single configuration"() {
        when:
        def artefact = new DefaultGrailsEndpointClass(DefaultEndpoint)

        then:
        artefact.servletName == 'CxfServlet'
    }

    def "servletName defaults to alphabetical on multiple configuration"() {
        setup:
            CxfConfigHandler.instance.cxfConfig.servlets = [
                'FirstServlet': '/services1/*',
                'SecondServlet': '/services2/*',
                'AThirdServlet': '/services3/*'
        ]

        when:
        def artefact = new DefaultGrailsEndpointClass(DefaultEndpoint)

        then:
        artefact.servletName == 'AThirdServlet'
    }

    def "servletName default can be changed"() {
        setup:
        CxfConfigHandler.instance.cxfConfig.servlets = [
                        'FirstServlet': '/services1/*',
                        'SecondServlet': '/services2/*',
                        'AThirdServlet': '/services3/*'
        ]

        when:
        CxfConfigHandler.instance.cxfConfig.servlet.defaultServlet = 'SecondServlet'
        def artefact = new DefaultGrailsEndpointClass(DefaultEndpoint)

        then:
        artefact.servletName == 'SecondServlet'
    }

    @Unroll
    def "servletName can be set #servletName = #expectedServletName"() {
        setup:
            CxfConfigHandler.instance.cxfConfig.servlets = [
                'FirstServlet': '/services1/*',
                'SecondServlet': '/services2/*',
                'AThirdServlet': '/services3/*'
        ]

        when:
        SimpleEndpoint.servletName = servletName
        def artefact = new DefaultGrailsEndpointClass(SimpleEndpoint)

        then:
        artefact.servletName == expectedServletName

        where:
        servletName     | expectedServletName
        ''              | 'AThirdServlet'
        null            | 'AThirdServlet'
        'FirstServlet'  | 'FirstServlet'
        'SecondServlet' | 'SecondServlet'
        'AThirdServlet' | 'AThirdServlet'
        'Foobar'        | 'AThirdServlet'
    }

}
