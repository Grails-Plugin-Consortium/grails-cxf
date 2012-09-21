package org.grails.cxf.test

import javax.jws.WebService
import javax.jws.WebMethod

@WebService
class PlaneService {

    @WebMethod
    Boolean canFly(){
        true
    }
}
