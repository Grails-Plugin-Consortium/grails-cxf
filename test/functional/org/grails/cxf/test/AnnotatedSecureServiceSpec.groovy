package org.grails.cxf.test

import geb.spock.GebReportingSpec
import spock.lang.Ignore
import wslite.soap.SOAPClient
import wslite.soap.SOAPClientException
import wslite.soap.SOAPResponse
import wslite.soap.SOAPVersion

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

    @Ignore("This works if you update the token and timestamps, but due to wss4j nonce spec rerunning is problematic atm.  Run with soap-ui and add username=wsuser, password=password and WSS-Password-Type of PasswordText to see the security in action.")
    def "invoke a method on wss4j secured service with creds"() {
        given:
        def username = "wsuser"
        def password = "password"

        //Fri Apr 24 12:58:08 CDT 2015:DEBUG:>> "
        // <soapenv:Header>
        // <wsse:Security soapenv:mustUnderstand="1" xmlns:wsse="http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd" xmlns:wsu="http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd">
        //      <wsse:UsernameToken wsu:Id="UsernameToken-75B63659BEDD8A861714298982881941">
        //          <wsse:Username>wsuser</wsse:Username>
        //          <wsse:Password Type="http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordText">password</wsse:Password>
        //          <wsse:Nonce EncodingType="http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-soap-message-security-1.0#Base64Binary">mzMOFF8Jsj6XR1y1kVVlzQ==</wsse:Nonce>
        //          <wsu:Created>2015-04-24T17:58:08.192Z</wsu:Created>
        //      </wsse:UsernameToken>
        //  </wsse:Security>
        // </soapenv:Header>"
        when:
        SOAPResponse response = client.send {
            envelopeAttributes "xmlns:test": 'http://test.cxf.grails.org/', "xmlns:soapenv":"soapenv"
            version SOAPVersion.V1_1
            header {
                'wsse:Security'('soapenv:mustUnderstand': "1", 'xmlns:wsse': 'http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd', 'xmlns:wsu': 'http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd') {
                    'wsse:UsernameToken'('wsu:Id':"UsernameToken-75B63659BEDD8A861714298982881941") {
                        'wsse:Username'(username)
                        'wsse:Password'('Type':'http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordText',password)
                        'wsse:Nonce'('EncodingType':'http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-soap-message-security-1.0#Base64Binary','mzMOFF8Jsj6XR1y1kVVlzQ==')
                        'wsu:Created'('2015-04-24T17:58:08.192Z')
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