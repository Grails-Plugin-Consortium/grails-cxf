package org.grails.cxf.artefact

import grails.plugin.spock.UnitSpec

class EndpointArtefactHandlerSpec extends UnitSpec {

    private class SomeEndpointNot {}

    private class EndpointOther {}

    private class SuperEndpoint {}

    private class SuperDuperEndpoint {}

    def "isArtefactClass matches classes"() {
        when:
            EndpointArtefactHandler handler = new EndpointArtefactHandler()

        then:
            handler.isArtefactClass(clazz) == isArtefactClass

        where:
            clazz               | isArtefactClass
            null                | false
            Object              | false
            String              | false
            SomeEndpointNot     | false
            EndpointOther       | false
            SuperEndpoint       | true
            SuperDuperEndpoint  | true
    }
}
