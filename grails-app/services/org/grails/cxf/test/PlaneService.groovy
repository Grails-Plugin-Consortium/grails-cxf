package org.grails.cxf.test

import javax.jws.WebService
import javax.jws.WebMethod
import javax.jws.WebResult

@WebService
class PlaneService {

    //todo: This will not work unless expose is defined as the default is to not ignore anything... not sure why
    static excludes = ['ignoreMe']
    static soap12 = true

    @WebMethod Boolean canFly(){
        true
    }

    @WebMethod Boolean canFloat(){
        false
    }

    String ignoreMe(Boolean bool){
        bool.toString()
    }
}
