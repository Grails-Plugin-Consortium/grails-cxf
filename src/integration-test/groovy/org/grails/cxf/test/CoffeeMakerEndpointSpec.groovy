package org.grails.cxf.test

import geb.spock.GebSpec
import grails.test.mixin.integration.Integration
import org.grails.cxf.test.soap.simple.CoffeeType

import spock.lang.Unroll
import wslite.soap.SOAPClient
import wslite.soap.SOAPFaultException
import wslite.soap.SOAPResponse
import wslite.soap.SOAPVersion

@Integration(applicationClass=grailscxf.Application)
class CoffeeMakerEndpointSpec extends GebSpec {

    SOAPClient client = new SOAPClient("http://localhost:${System.getProperty("server.port", "8080")}/grails-cxf/services/coffeeMaker")

    def "get coffee locations map"() {
        when:
        SOAPResponse response = client.send {
            envelopeAttributes "xmlns:test": 'http://test.cxf.grails.org/'
            body {
                'test:mapCoffeeLocations' {}
            }
        }
        def returnNode = response.mapCoffeeLocationsResponse.return.entry

        then:
        200 == response.httpResponse.statusCode
        SOAPVersion.V1_1 == response.soapVersion
        returnNode[0].key.toString() == 'Colombia'
        returnNode[0].value.toString() == 'Colombian'
        returnNode[1].key.toString() == 'Ethiopia'
        returnNode[1].value.toString() == 'Ethiopian'
    }

    @Unroll
    def "find coffee type by name #coffeeName"() {
        when:
        SOAPResponse response = client.send {
            envelopeAttributes "xmlns:test": 'http://test.cxf.grails.org/'
            body {
                'test:findCoffeeTypeByName' {
                    name(coffeeName)
                }
            }
        }
        def returnNode = response.findCoffeeTypeByNameResponse.return

        then:
        200 == response.httpResponse.statusCode
        SOAPVersion.V1_1 == response.soapVersion
        coffeeName == returnNode.text()

        where:
        coffeeName << ['Arabica', 'Robusta', 'Blue_Mountain', 'Colombian', 'Ethiopian']
    }

    def "list coffee types"() {
        when:
        SOAPResponse response = client.send {
            envelopeAttributes "xmlns:test": 'http://test.cxf.grails.org/'
            body {
                'test:listCoffeeTypes' {}
            }
        }
        def returnNode = response.listCoffeeTypesResponse

        then:
        200 == response.httpResponse.statusCode
        SOAPVersion.V1_1 == response.soapVersion
        returnNode.each { coffee ->
            CoffeeType.find { it.toString() == coffee.text() }
        }
    }

    @Unroll
    def "make coffee #type"() {
        when:
        SOAPResponse response = client.send {
            envelopeAttributes "xmlns:test": 'http://test.cxf.grails.org/'
            body {
                'test:turnOn' {}
            }
        }

        response = client.send {
            envelopeAttributes "xmlns:test": 'http://test.cxf.grails.org/'
            body {
                'test:makeCoffee' {
                    coffeeType(type.toString())
                }
            }
        }
        def returnNode = response.makeCoffeeResponse.return

        then:
        200 == response.httpResponse.statusCode
        SOAPVersion.V1_1 == response.soapVersion
        returnNode.text() == "Making coffee with ${type.toString()} beans."

        where:
        type << CoffeeType.values().toList()
    }

    def "making coffee before turning on maker should fault"() {
        when:
        SOAPResponse response = client.send {
            envelopeAttributes "xmlns:test": 'http://test.cxf.grails.org/'
            body {
                'test:turnOff' {}
            }
        }

        response = client.send {
            envelopeAttributes "xmlns:test": 'http://test.cxf.grails.org/'
            body {
                'test:makeCoffee' {
                    coffeeType(CoffeeType.Arabica.toString())
                }
            }
        }

        then:
        def sfe = thrown(SOAPFaultException)
        sfe.message == "soap:Server -  The Coffee Maker is not turned on.. Expression: makerOn.get()"
        sfe.fault == "soap:Server The Coffee Maker is not turned on.. Expression: makerOn.get()"
    }
}
