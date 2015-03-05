package org.grails.cxf.test

import geb.spock.GebSpec
import grails.test.mixin.integration.Integration
import org.grails.cxf.test.soap.CustomerType
import wslite.soap.SOAPClient
import wslite.soap.SOAPResponse
import wslite.soap.SOAPVersion

@Integration(applicationClass=grailscxf.Application)
class CustomerServiceWsdlEndpointSpec extends GebSpec {

    SOAPClient client = new SOAPClient("http://localhost:${System.getProperty("server.port", "8080")}/grails-cxf/services/customerServiceWsdl")

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
        customerName  | customerType
        'Frank'       | CustomerType.PRIVATE.name()
        'Super Duper' | CustomerType.BUSINESS.name()

    }
}
