package org.grails.cxf.test

import geb.spock.GebReportingSpec
import wslite.soap.SOAPClient
import wslite.soap.SOAPResponse

/**
 */
class CoffeeMakerEndpointSpec extends GebReportingSpec {

    SOAPClient client = new SOAPClient('http://localhost:8080/cxf/services/coffeeMaker')

    def "get coffee location map"() {
        when:
        SOAPResponse response = client.send {
            envelopeAttributes "xmlns:test": 'http://test.cxf.grails.org/'
            body {
                'test:mapCoffeeLocations'
            }
        }
        def methodResponse = response.body.mapCoffeeLocationsResponse.coffeeLocations
        println methodResponse

        then:
        200 == response.httpResponse.statusCode
//        SOAPVersion.V1_1 == response.soapVersion
//        'The Definitive Book of Awesomeness' == methodResponse.title.text()
//        '1-84356-028-3' == methodResponse.isbn.text()
    }

    def "findBookByIsbn should fault when isbn is invalid"() {

    }

}
