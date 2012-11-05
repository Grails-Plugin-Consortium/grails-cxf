package org.grails.cxf.test

import org.grails.cxf.utils.EndpointType
import javax.jws.WebMethod
import org.grails.cxf.test.soap.simple.SimpleException
import javax.jws.WebResult

class SimpleExceptionService {

    static expose = EndpointType.JAX_WS

    @WebMethod
    Boolean pass(){
        true
    }

    @WebMethod(operationName = 'fail')
    @WebResult(name='exceptions')
    List<SimpleException> fail(){
        [new SimpleException("I like turtles"), new SimpleException("I like zombies")]
    }
}
