package org.grails.cxf.test

import geb.spock.GebReportingSpec

import spock.lang.Unroll
import wslite.soap.SOAPClient
import wslite.soap.SOAPResponse
import wslite.soap.SOAPVersion

class LegacyCxfServiceSpec extends GebReportingSpec {

    SOAPClient client = new SOAPClient("http://localhost:${System.getProperty("server.port", "8080")}/grails-cxf/services/legacyCxf")

    @Unroll
    def "invoke the exposed method on the legacy cxf service param=#legacyParam"() {
        when:
        //for some reason this one needs the namespace on the param, whereas the others do not
        SOAPResponse response = client.send {
            envelopeAttributes "xmlns:test": 'http://test.cxf.grails.org/'
            body {
                'test:legacyMethod' {
                    'test:param'(legacyParam)
                }
            }
        }
        def methodResponse = response.body.legacyMethodResponse.return

        then:
        200 == response.httpResponse.statusCode
        SOAPVersion.V1_1 == response.soapVersion
        "legacy ${legacyParam}" == methodResponse.text()

        where:
        legacyParam << ['hello', 'foo', 'world']
    }
}
