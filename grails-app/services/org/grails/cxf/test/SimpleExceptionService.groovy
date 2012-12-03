package org.grails.cxf.test

import javax.jws.WebMethod
import javax.jws.WebResult

import org.grails.cxf.test.soap.simple.SimpleException
import org.grails.cxf.utils.EndpointType

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
