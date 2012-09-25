package org.grails.cxf.test

import org.grails.cxf.utils.EndpointType
import javax.jws.WebMethod

class BoatService {

    //jsx_ws requires @WebMethod for exposure of service methods
    static expose = EndpointType.JAX_WS

    @WebMethod
    String waterski() {
        'SKI'
    }

    String fish(){
        'FISH'
    }
}
