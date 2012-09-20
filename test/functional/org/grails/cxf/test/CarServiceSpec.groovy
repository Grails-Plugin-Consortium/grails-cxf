package org.grails.cxf.test

import geb.spock.GebReportingSpec
import wslite.soap.SOAPClient
import wslite.soap.SOAPResponse
import wslite.soap.SOAPVersion

/**
 */
class CarServiceSpec extends GebReportingSpec {

    SOAPClient client = new SOAPClient('http://localhost:8080/cxf/services/car')

    def "honk the horn"() {
        when:
        SOAPResponse response = client.send {
            envelopeAttributes "xmlns:test": 'http://test.cxf.grails.org/'
            body {
                'test:honkHorn' {
                }
            }
        }
        def methodResponse = response.body.honkHornResponse

        then:
        200 == response.httpResponse.statusCode
        SOAPVersion.V1_1 == response.soapVersion
        'The Definitive Book of Awesomeness' == methodResponse.title.text()
        '1-84356-028-3' == methodResponse.isbn.text()
    }

    def "findBookByIsbn should fault when isbn is invalid"() {

    }

}
