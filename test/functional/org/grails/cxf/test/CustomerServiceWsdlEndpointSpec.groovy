package org.grails.cxf.test

import geb.spock.GebReportingSpec
import wslite.soap.SOAPClient
import wslite.soap.SOAPResponse
import wslite.soap.SOAPVersion
import org.grails.cxf.test.soap.CustomerType

class CustomerServiceWsdlEndpointSpec extends GebReportingSpec {

    SOAPClient client = new SOAPClient('http://localhost:8080/cxf/services/customerServiceWsdl?wsdl')

    def "getCustomersByName actually works"() {
        when:
            SOAPResponse response = client.send {
                envelopeAttributes "xmlns:test": "http://test.cxf.grails.org/"
                body {
                    'test:getCustomersByName' {
                        name customerName
                    }
                }
            }
            def methodResponse = response.body.getCustomersByNameResponse.return

        then:
            response.httpResponse.statusCode == 200
            response.soapVersion == SOAPVersion.V1_1
            customerName == methodResponse.name.text()
            customerType == methodResponse.type.text()

        where:
            customerName    | customerType
            'Frank'         | CustomerType.PRIVATE.name()
            'Super Duper'   | CustomerType.BUSINESS.name()

    }
}
