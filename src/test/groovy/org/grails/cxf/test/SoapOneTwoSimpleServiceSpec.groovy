package org.grails.cxf.test

import geb.spock.GebSpec
import grails.test.mixin.integration.Integration
import wslite.soap.SOAPClient
import wslite.soap.SOAPResponse
import wslite.soap.SOAPVersion

@Integration(applicationClass=grailscxf.Application)
class SoapOneTwoSimpleServiceSpec extends GebSpec {

    SOAPClient client = new SOAPClient("http://localhost:${System.getProperty("server.port", "8080")}/grails-cxf/services/soapOneTwoSimple")

    def "invoke a method on the service using soap 1.2"() {
        when:
        SOAPResponse response = client.send {
            envelopeAttributes "xmlns:test": 'http://test.cxf.grails.org/'
            version SOAPVersion.V1_2
            body {
                'test:simpleMethod'{
                    'test:param'(legacyParam)
                }
            }
        }
        def methodResponse = response.body.simpleMethodResponse.return

        then:
        200 == response.httpResponse.statusCode
        SOAPVersion.V1_2 == response.soapVersion
        legacyParam == methodResponse.text()

        where:
        legacyParam << ['hello', 'foo', 'world']
    }

    def "invoking a soap 1.2 service with 1.1 should be backwards compatible"() {
        when:
        SOAPResponse response = client.send {
            envelopeAttributes "xmlns:test": 'http://test.cxf.grails.org/'
            version SOAPVersion.V1_1
            body {
                'test:simpleMethod'{
                    'test:param'(legacyParam)
                }
            }
        }
        def methodResponse = response.body.simpleMethodResponse.return

        then:
        200 == response.httpResponse.statusCode
        SOAPVersion.V1_1 == response.soapVersion
        legacyParam == methodResponse.text()

        where:
        legacyParam << ['hello', 'foo', 'world']
    }
}
