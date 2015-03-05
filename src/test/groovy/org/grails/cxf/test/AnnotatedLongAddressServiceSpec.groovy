package org.grails.cxf.test

import geb.spock.GebSpec
import grails.test.mixin.integration.Integration
import wslite.soap.SOAPClient
import wslite.soap.SOAPResponse
import wslite.soap.SOAPVersion

@Integration(applicationClass=grailscxf.Application)
class AnnotatedLongAddressServiceSpec extends GebSpec {

    SOAPClient client = new SOAPClient("http://localhost:${System.getProperty("server.port", "8080")}/grails-cxf/services/i/love/turtles/v1/zombie")

    def "invoke a method on the annotated service with long address and overridden name"() {
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
