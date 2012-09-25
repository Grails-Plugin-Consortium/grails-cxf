package org.grails.cxf.test

import geb.spock.GebReportingSpec
import wslite.soap.SOAPClient
import wslite.soap.SOAPResponse
import wslite.soap.SOAPVersion
import wslite.soap.SOAPFaultException

class BookStoreEndpointSpec extends GebReportingSpec {

    SOAPClient client = new SOAPClient("http://localhost:${System.getProperty("server.port", "8080")}/cxf/services/bookStore")

    def "findBookByIsbn should return the book of awesomeness given a valid isbn"() {
        when:
        SOAPResponse response = client.send {
            envelopeAttributes "xmlns:test": 'http://test.cxf.grails.org/'
            body {
                'test:findBookByIsbn' {
                    isbn {
                        number '1-84356-028-3'
                    }
                }
            }
        }
        def methodResponse = response.body.findBookByIsbnResponse.book

        then:
        200 == response.httpResponse.statusCode
        SOAPVersion.V1_1 == response.soapVersion
        'The Definitive Book of Awesomeness' == methodResponse.title.text()
        '1-84356-028-3' == methodResponse.isbn.text()
    }

    def "findBookByIsbn should fault when isbn is invalid"() {
        when:
        SOAPResponse response = client.send {
            envelopeAttributes "xmlns:test": 'http://test.cxf.grails.org/'
            body {
                'test:findBookByIsbn' {
                    isbn {
                        number '55378008'
                    }
                }
            }
        }

        then:
        def sfe = thrown(SOAPFaultException)
        sfe.fault == "soap:ServerFault occurred while processing."
    }
}
