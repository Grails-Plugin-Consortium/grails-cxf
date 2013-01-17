package org.grails.cxf.test

import geb.spock.GebReportingSpec
import wslite.soap.SOAPClient
import wslite.soap.SOAPResponse
import wslite.soap.SOAPVersion
import org.apache.ws.security.WSSecurityException
import wslite.soap.SOAPClientException

class AnnotatedSecureServiceSpec extends GebReportingSpec {

    SOAPClient client = new SOAPClient("http://localhost:${System.getProperty("server.port", "8080")}/grails-cxf/services/annotatedSecure")

    def "invoke a method on wss4j secured service"() {
        when:
        SOAPResponse response = client.send {
            envelopeAttributes "xmlns:test": 'http://test.cxf.grails.org/'
            version SOAPVersion.V1_1
            body {
                'test:simpleMethod' {
                    param('hello')
                }
            }
        }

        then:
        def ex = thrown(SOAPClientException)
        ex.message != ""
    }
}
