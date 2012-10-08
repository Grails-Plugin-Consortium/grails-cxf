package org.grails.cxf.test

import org.grails.cxf.utils.EndpointType

import javax.jws.WebMethod
import javax.jws.WebResult

class BoatService {

    //jsx_ws requires @WebMethod for exposure of service methods
    static expose = EndpointType.JAX_WS

    def truckService

    @WebResult(name = 'sound')
    @WebMethod(operationName = 'waterOnFreeway')
    String waterOnFreeway() {
        return truckService.crushCars()
    }

    @WebMethod
    String waterski() {
        'SKI'
    }

    String fish() {
        'FISH'
    }
}
