package org.grails.cxf.test

import org.grails.cxf.utils.EndpointType
import org.grails.cxf.utils.GrailsCxfEndpoint
import javax.jws.WebMethod
import javax.jws.WebResult
import javax.jws.WebParam

/**
 */
@GrailsCxfEndpoint(address = "/i/love/turtles/v1/zombie")
class AnnotatedLongAddressService {

    @WebMethod(operationName="simpleMethod")
    @WebResult(name="simpleResult")
    String simpleMethod(@WebParam(name="param") String param) {
        return param.toString()
    }
}
