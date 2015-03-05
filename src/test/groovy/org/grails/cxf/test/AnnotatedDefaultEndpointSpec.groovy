package org.grails.cxf.test

import geb.spock.GebSpec
import grails.test.mixin.integration.Integration
import wslite.soap.SOAPClient
import wslite.soap.SOAPResponse
import wslite.soap.SOAPVersion

@Integration(applicationClass=grailscxf.Application)
class AnnotatedDefaultEndpointSpec extends GebSpec {

    SOAPClient client = new SOAPClient("http://localhost:${System.getProperty("server.port", "8080")}/grails-cxf/services/annotatedDefault")

    def "invoke a method on the annotated service using annotation defaults"() {
        when:
        SOAPResponse response = client.send {
            envelopeAttributes "xmlns:test": 'http://test.cxf.grails.org/'
            version SOAPVersion.V1_1
            body {
                'test:simpleMethod' {
                    param(legacyParam)
                }
            }
        }
        def methodResponse = response.body.simpleMethodResponse.simpleResult

        then:
        200 == response.httpResponse.statusCode
        SOAPVersion.V1_1 == response.soapVersion
        legacyParam == methodResponse.text()

        where:
        legacyParam << ['hello', 'foo', 'world']
    }
}
