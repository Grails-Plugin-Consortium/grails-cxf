package test

import geb.spock.GebReportingSpec
import wslite.soap.SOAPClient
import wslite.soap.SOAPFaultException

/**
 */
class CxfFunctionalSpec extends GebReportingSpec {

    def "invoke cxf test service soap endpoint stringMethod"() {
        given:
        def cxfClient = new SOAPClient('http://localhost:8080/cxf/services/testCxf?wsdl')

        when:
        def response = cxfClient.send(
                """<?xml version='1.0' encoding='UTF-8'?>
                    <soap-env:Envelope xmlns:soap-env='http://schemas.xmlsoap.org/soap/envelope/' xmlns:test='http://test/'>
                      <soap-env:Header />
                      <soap-env:Body>
                        <test:stringMethod>
                          <test:string>hello</test:string>
                        </test:stringMethod>
                      </soap-env:Body>
                    </soap-env:Envelope>""")

        then:
        200 == response.httpResponse.statusCode
        'hello' == response.envelope as String
    }

    def "invoke cxf test service soap endpoint booleanMethod"() {
        given:
        def cxfClient = new SOAPClient('http://localhost:8080/cxf/services/testCxf?wsdl')

        when:
        def response = cxfClient.send(
                """<?xml version='1.0' encoding='UTF-8'?>
                    <soap-env:Envelope xmlns:soap-env='http://schemas.xmlsoap.org/soap/envelope/' xmlns:test='http://test/'>
                      <soap-env:Header />
                      <soap-env:Body>
                        <test:booleanMethod>
                          <test:bool>true</test:bool>
                        </test:booleanMethod>
                      </soap-env:Body>
                    </soap-env:Envelope>"""
        )

        then:
        200 == response.httpResponse.statusCode
        response.envelope as Boolean
    }

    def "invoke cxf test service soap endpoint sonicScrewdriver"() {
        given:
        def cxfClient = new SOAPClient('http://localhost:8080/cxf/services/testCxf?wsdl')

        when:
        def response = cxfClient.send(
                """<?xml version='1.0' encoding='UTF-8'?>
                    <soap-env:Envelope xmlns:soap-env='http://schemas.xmlsoap.org/soap/envelope/' xmlns:test='http://test/'>
                      <soap-env:Header />
                      <soap-env:Body>
                        <test:sonicScrewdriver />
                      </soap-env:Body>
                    </soap-env:Envelope>"""
        )

        then: 'this should not exist as a valid endpoint'
        SOAPFaultException exception = thrown()
        exception instanceof SOAPFaultException
        exception.message.contains('was not recognized')
    }

    def "invoke cxfjax test service soap endpoint stringMethod"() {
        given:
        def cxfClient = new SOAPClient('http://localhost:8080/cxf/services/testCxfJax?wsdl')

        when:
        def response = cxfClient.send(
                """<?xml version='1.0' encoding='UTF-8'?>
                        <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:test="http://test/">
                            <soapenv:Header/>
                            <soapenv:Body>
                                <test:stringMethod>
                                    <string>hello</string>
                                </test:stringMethod>
                            </soapenv:Body>
                        </soapenv:Envelope>""")

        then:
        200 == response.httpResponse.statusCode
        'hello' == response.envelope as String
    }

    def "invoke cxfjax test service soap endpoint booleanMethod"() {
        given:
        def cxfClient = new SOAPClient('http://localhost:8080/cxf/services/testCxfJax?wsdl')

        when:
        def response = cxfClient.send(
                """<?xml version='1.0' encoding='UTF-8'?>
                   <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:test="http://test/">
                       <soapenv:Header/>
                       <soapenv:Body>
                          <test:booleanMethod>
                             <bool>true</bool>
                          </test:booleanMethod>
                       </soapenv:Body>
                    </soapenv:Envelope>""")

        then:
        200 == response.httpResponse.statusCode
        response.envelope as Boolean
    }

    def "invoke cxfjax test service soap endpoint sonicScrewdriver"() {
        given:
        def cxfClient = new SOAPClient('http://localhost:8080/cxf/services/testCxfJax?wsdl')

        when:
        def response = cxfClient.send(
                """<?xml version='1.0' encoding='UTF-8'?>
                    <soap-env:Envelope xmlns:soap-env='http://schemas.xmlsoap.org/soap/envelope/' xmlns:test='http://test/'>
                      <soap-env:Header />
                      <soap-env:Body>
                        <test:sonicScrewdriver />
                      </soap-env:Body>
                    </soap-env:Envelope>"""
        )

        then: 'this should not exist as a valid endpoint'
        SOAPFaultException exception = thrown()
        exception instanceof SOAPFaultException
        exception.message.contains('was not recognized')
    }
}
