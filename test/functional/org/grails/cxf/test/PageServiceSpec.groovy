package org.grails.cxf.test

import geb.spock.GebReportingSpec

import wslite.soap.SOAPClient
import wslite.soap.SOAPResponse

class PageServiceSpec extends GebReportingSpec {

    SOAPClient client = new SOAPClient("http://localhost:${System.getProperty("server.port", "8080")}/cxf/services/page")

    def "invoke a method where domain objects are persisted with hasMany"() {
        when:
        SOAPResponse response = client.send {
            envelopeAttributes "xmlns:test": 'http://test.cxf.grails.org/'
            body {
                'test:getMeSomePersistedPagesWithWords' {}
            }
        }
        def methodResponse = response.body.getMeSomePersistedPagesWithWordsResponse

        then:
        200 == response.httpResponse.statusCode
        methodResponse.page.size() == 3
        methodResponse.page[0].id == 1
        methodResponse.page[0].version == 0
        methodResponse.page[0].words.word.size() == 4
    }

    def "invoke a method where domain objects are not persisted with hasMany"() {
        when:
        SOAPResponse response = client.send {
            envelopeAttributes "xmlns:test": 'http://test.cxf.grails.org/'
            body {
                'test:getMeSomePagesWithWords' {}
            }
        }
        def methodResponse = response.body.getMeSomePagesWithWordsResponse


        then:
        200 == response.httpResponse.statusCode
        methodResponse.page.size() == 3
        println methodResponse.page[0].text()
        methodResponse.page[0].words.word.size() == 4

        then: 'these will not be on the xml graph if not persisted'
        !methodResponse.page[0].childNodes().find { it.name == "id" }
        !methodResponse.page[0].childNodes().find { it.name == "version" }
    }

}
