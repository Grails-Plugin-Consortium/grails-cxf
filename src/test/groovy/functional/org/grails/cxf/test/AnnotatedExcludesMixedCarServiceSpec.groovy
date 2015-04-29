package org.grails.cxf.test

import geb.spock.GebReportingSpec
import wslite.soap.SOAPClient
import wslite.soap.SOAPResponse
import wslite.soap.SOAPVersion
import wslite.soap.SOAPFaultException

class AnnotatedExcludesMixedCarServiceSpec extends GebReportingSpec {

    SOAPClient client = new SOAPClient("http://localhost:${System.getProperty("server.port", "8080")}/grails-cxf/services/annotatedExcludesMixedCar")

    def "honk the horn"() {
        when:
        SOAPResponse response = client.send {
            envelopeAttributes "xmlns:test": 'http://test.cxf.grails.org/'
            body {
                'test:honkHorn' {}
            }
        }
        def methodResponse = response.body.honkHornResponse

        then:
        200 == response.httpResponse.statusCode
        SOAPVersion.V1_1 == response.soapVersion
        'HONK' == methodResponse.toString()
    }

    def "dont honk the horn"() {
        when:
        SOAPResponse response = client.send {
            envelopeAttributes "xmlns:test": 'http://test.cxf.grails.org/'
            body {
                'test:dontHonk' {}
            }
        }

        then:
        thrown(SOAPFaultException)
    }

    def "start the car"() {
        when:
        SOAPResponse response = client.send {
            envelopeAttributes "xmlns:test": 'http://test.cxf.grails.org/'
            body {
                'test:start' {}
            }
        }
        def methodResponse = response.body.startResponse

        then:
        200 == response.httpResponse.statusCode
        SOAPVersion.V1_1 == response.soapVersion
        'GAS' == methodResponse.toString()
    }

    def "stop the car"() {
        when:
        SOAPResponse response = client.send {
            envelopeAttributes "xmlns:test": 'http://test.cxf.grails.org/'
            body {
                'test:stop' {}
            }
        }
        def methodResponse = response.body.stopResponse

        then:
        200 == response.httpResponse.statusCode
        SOAPVersion.V1_1 == response.soapVersion
        'BRAKES' == methodResponse.toString()
    }
}
