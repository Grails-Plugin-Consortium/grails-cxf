package org.grails.cxf.test

import geb.spock.GebReportingSpec
import wslite.soap.SOAPClient
import wslite.soap.SOAPClientException
import wslite.soap.SOAPResponse
import wslite.soap.SOAPVersion
import org.apache.ws.security.util.Base64
import org.opensaml.ws.wssecurity.Username
import wslite.http.auth.HTTPBasicAuthorization

class AnnotatedSecureServiceSpec extends GebReportingSpec {

    SOAPClient client = new SOAPClient("http://localhost:${System.getProperty("server.port", "8080")}/grails-cxf/services/annotatedSecure")

    def "invoke a method on wss4j secured service without creds"() {
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

    def "invoke a method on wss4j secured service with creds"() {
        given:
        def username = "wsuser"
        def password = "secret"

        when:
        SOAPResponse response = client.send {
            envelopeAttributes "xmlns:test": 'http://test.cxf.grails.org/', "xmlns:soapenv":"soapenv"
            version SOAPVersion.V1_1
            header {
                'wsse:Security'('soapenv:mustUnderstand': "1", 'xmlns:wsse': 'http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd', 'xmlns:wsu': 'http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd') {
                    'wsse:UsernameToken'('wsu:Id':"UsernameToken-13") {
                        'wsse:Username'(username)
                        'wsse:Password'('Type':'http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordText',password)
                        'wsse:Nonce'('EncodingType':'http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-soap-message-security-1.0#Base64Binary',new String(password.bytes.encodeBase64().toString()))
                        'wsu:Created'('2013-01-18T16:19:17.950Z')
                    }
                }
            }
            body {
                'test:simpleMethod' {
                    param(legacyParam)
                }
            }
        }
        def methodResponse = response.body.simpleMethodResponse.simpleResult

        then:
        200 == response.httpResponse.statusCode
        SOAPVersion.V1_1 == response.soapVersion
        legacyParam == methodResponse.text()

        where:
        legacyParam << ['hello', 'foo', 'world']
    }
}