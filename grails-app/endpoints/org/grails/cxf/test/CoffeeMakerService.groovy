package org.grails.cxf.test

import javax.jws.WebService
import javax.jws.WebMethod
import javax.jws.WebResult

/**
 */
@WebService
public interface CoffeeMakerService {

    @WebResult(name='coffeeLocations')
    @WebMethod mapCoffeeLocations()

}