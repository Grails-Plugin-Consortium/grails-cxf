package org.grails.cxf.test

import org.grails.cxf.test.soap.simple.CoffeeType

import java.util.concurrent.atomic.AtomicBoolean

/**
 * An example of a Simple Cxf SOAP Service.
 * <p>
 * Simple Cxf frontends are generally not recommended since they use reflection to determine the service
 * definition. More details can be found in the Cxf documentation.
 *
 * @see http://cxf.apache.org/docs/simple-frontend.html
 */
class CoffeeMakerEndpoint implements CoffeeMakerService {

    static exposeAs = 'JAX_WS'
//    static exposeAs = 'JAX_RS'

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

    Map<String, CoffeeType> mapCoffeeLocations() {
        return ['Colombia': CoffeeType.Colombian, 'Ethiopia': CoffeeType.Ethiopian]
    }

}
