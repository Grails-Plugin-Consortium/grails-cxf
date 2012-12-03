package org.grails.cxf.test

import geb.spock.GebReportingSpec

import wslite.soap.SOAPClient
import wslite.soap.SOAPFaultException
import wslite.soap.SOAPResponse
import wslite.soap.SOAPVersion

class BoatServiceSpec extends GebReportingSpec {

    SOAPClient client = new SOAPClient("http://localhost:${System.getProperty("server.port", "8080")}/cxf/services/boat")

    def "invoke the exposed method on the boat service"() {
        when:
        SOAPResponse response = client.send {
            envelopeAttributes "xmlns:test": 'http://test.cxf.grails.org/'
            body {
                'test:waterski' {}
            }
        }
        def methodResponse = response.body.waterskiResponse.return

        then:
        200 == response.httpResponse.statusCode
        SOAPVersion.V1_1 == response.soapVersion
        'SKI' == methodResponse.text()
    }

    def "invoke the hidden method on the boat service"() {
        when:
        client.send {
            envelopeAttributes "xmlns:test": 'http://test.cxf.grails.org/'
            body {
                'test:fish' {}
            }
        }

        then:
        thrown(SOAPFaultException)
    }

    def "invoke v1.1 service with soap v1.2 should fail"() {
        when:
        client.send {
            envelopeAttributes "xmlns:test": 'http://test.cxf.grails.org/'
            version SOAPVersion.V1_2
            body {
                'test:waterski' {}
            }
        }

        then:
        thrown(SOAPFaultException)
    }
}
