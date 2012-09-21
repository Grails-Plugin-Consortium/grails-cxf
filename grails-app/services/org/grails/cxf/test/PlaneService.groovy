package org.grails.cxf.test

import javax.jws.WebService
import javax.jws.WebMethod

@WebService
class PlaneService {

    //todo: figure out why this isn't working with non-jax-rs type service when @WebService autowired
    static excludes = ['canFloat', 'ignoreMe']

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
