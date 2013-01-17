package org.grails.cxf.test

import org.grails.cxf.test.soap.simple.CoffeeType

import java.util.concurrent.atomic.AtomicBoolean
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter
import org.grails.cxf.adapter.GrailsCxfMapAdapter
import org.grails.cxf.utils.EndpointType
import javax.jws.WebResult
import org.grails.cxf.test.soap.security.CustomLoggingInInterceptor

/**
 * An example of a Simple Cxf SOAP Service.
 * <p>
 * Simple Cxf frontends are generally not recommended since they use reflection to determine the service
 * definition. More details can be found in the Cxf documentation.
 *
 * @see http://cxf.apache.org/docs/simple-frontend.html
 */
class CoffeeMakerEndpoint {

    static expose = EndpointType.SIMPLE

    AtomicBoolean makerOn = new AtomicBoolean(false)

    Boolean turnOn() {
        return !makerOn.getAndSet(true)
    }

    Boolean turnOff() {
        return !makerOn.getAndSet(false)
    }

    String makeCoffee(CoffeeType coffeeType) {
        assert makerOn.get(), " The Coffee Maker is not turned on."
        return "Making coffee with $coffeeType beans."
    }

    CoffeeType findCoffeeTypeByName(String name) {
        return CoffeeType.values().find() { it.name() == name }
    }

    List<CoffeeType> listCoffeeTypes() {
        return CoffeeType.values()
    }

    @XmlJavaTypeAdapter(GrailsCxfMapAdapter.class)
    @WebResult(name='entires')
    Map<String, CoffeeType> mapCoffeeLocations() {
        return ['Colombia': CoffeeType.Colombian, 'Ethiopia': CoffeeType.Ethiopian]
    }

}
