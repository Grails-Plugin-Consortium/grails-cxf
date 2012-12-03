package org.grails.cxf.test

import geb.spock.GebReportingSpec

import wslite.soap.SOAPClient
import wslite.soap.SOAPResponse
import wslite.soap.SOAPVersion

class PlaneServiceSpec extends GebReportingSpec {

    SOAPClient client = new SOAPClient("http://localhost:${System.getProperty("server.port", "8080")}/grails-cxf/services/plane")

    def "invoke a boolean method on the plane service using soap 1.2"() {
        when:
        SOAPResponse response = client.send {
            envelopeAttributes "xmlns:test": 'http://test.cxf.grails.org/'
            version SOAPVersion.V1_2
            body {
                'test:canFly' {}
            }
        }
        def methodResponse = response.body.canFlyResponse.return

        then:
        200 == response.httpResponse.statusCode
        SOAPVersion.V1_2 == response.soapVersion
        'true' == methodResponse.text()
    }

    def "invoke another boolean method on the plane service using soap 1.2"() {
        when:
        SOAPResponse response = client.send {
            envelopeAttributes "xmlns:test": 'http://test.cxf.grails.org/'
            version SOAPVersion.V1_2
            body {
                'test:canFloat' {}
            }
        }
        def methodResponse = response.body.canFloatResponse.return

        then:
        200 == response.httpResponse.statusCode
        SOAPVersion.V1_2 == response.soapVersion
        'false' == methodResponse.text()
    }

    def "invoking a soap 1.2 service with 1.1 should be backwards compatible"() {
        when:
        SOAPResponse response = client.send {
            envelopeAttributes "xmlns:test": 'http://test.cxf.grails.org/'
            version SOAPVersion.V1_1
            body {
                'test:canFloat' {}
            }
        }
        def methodResponse = response.body.canFloatResponse.return

        then:
        200 == response.httpResponse.statusCode
        SOAPVersion.V1_1 == response.soapVersion
        'false' == methodResponse.text()
    }
}
