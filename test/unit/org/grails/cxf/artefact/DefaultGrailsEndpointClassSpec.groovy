package org.grails.cxf.artefact

import grails.plugin.spock.UnitSpec
import org.grails.cxf.utils.CxfConfigHandler

import static org.grails.cxf.artefact.EndpointExposureType.CXF
import static org.grails.cxf.artefact.EndpointExposureType.JAXRS
import static org.grails.cxf.artefact.EndpointExposureType.JAXWS
import static org.grails.cxf.artefact.GrailsEndpointClass.DEFAULT_GROOVY_EXCLUDES

class DefaultGrailsEndpointClassSpec extends UnitSpec {

    private class DefaultEndpoint {}

    private class SimpleEndpoint {
        static exposeAs = ""
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
        SimpleEndpoint.exposeAs = ""
        SimpleEndpoint.excludes = []
        SimpleEndpoint.servletName = ""
    }

    def cleanup() {
        CxfConfigHandler.instance.cxfConfig = null
    }

    def "exposeAs properly configures by default"() {
        when:
            def artefact = new DefaultGrailsEndpointClass(DefaultEndpoint)

        then:
            artefact.exposeAs == CXF
    }

    def "exposeAs can be configured"() {
        when:
            SimpleEndpoint.exposeAs = exposeAs
            def artefact = new DefaultGrailsEndpointClass(SimpleEndpoint)

        then:
            artefact.exposeAs == expectedExposeAs

        where:
            exposeAs     | expectedExposeAs
            ''           | CXF
            null         | CXF
            1234         | CXF
            new Object() | CXF
            'CXF'        | CXF
            'cxf'        | CXF
            'JAXWS'      | JAXWS
            'jaxws'      | JAXWS
            'JAXRS'      | JAXRS
            'jaxrs'      | JAXRS
            'FOO'        | CXF
            'foo'        | CXF
    }

    def "excludes is built with defaults"() {
        when:
            def artefact = new DefaultGrailsEndpointClass(DefaultEndpoint)

        then:
            artefact.excludes == DEFAULT_GROOVY_EXCLUDES as Set
    }

    def "excludes can be added to"() {
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

    def "proper postfix removal"() {
        when:
            def artefact = new DefaultGrailsEndpointClass(endpointClass)

        then:
            artefact.getNameNoPostfix() == expectedName

        where:
            endpointClass           | expectedName
            SimpleEndpoint          | 'simple'
            DefaultEndpoint         | 'default'
            TestEndpoint            | 'test'
            TheEndpointFooEndpoint  | 'theEndpointFoo'
            ToastAndButter          | 'toastAndButter'
    }

    def "sevletName defaults on single configuration"() {
        when:
            def artefact = new DefaultGrailsEndpointClass(DefaultEndpoint)

        then:
            artefact.servletName == 'CXFServlet'
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
            CxfConfigHandler.instance.cxfConfig.servlet.defaultName = 'SecondServlet'
            def artefact = new DefaultGrailsEndpointClass(DefaultEndpoint)

        then:
            artefact.servletName == 'SecondServlet'
    }

    def "servletName can be set"() {
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
            servletName         | expectedServletName
            ''                  | 'AThirdServlet'
            null                | 'AThirdServlet'
            'FirstServlet'      | 'FirstServlet'
            'SecondServlet'     | 'SecondServlet'
            'AThirdServlet'     | 'AThirdServlet'
            'Foobar'            | 'AThirdServlet'
    }

}
