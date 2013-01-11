package org.grails.cxf.test

import javax.jws.WebMethod
import javax.jws.WebParam
import javax.jws.WebResult
import org.grails.cxf.utils.EndpointType

class CxfJaxAddressService {

    static expose = EndpointType.JAX_WS
    static address = "/v1.4/#name"

    @WebResult(name='legacyResult')
    @WebMethod(operationName='legacyMethod')
    String legacyMethod(@WebParam(name='param') String param) {
        //cxf doesn't like GStringImpl so make sure to convert to java.lang.String
        return "legacy ${param}".toString()
    }

    String hiddenMethod(String param){
        return "hidden ${param}".toString()
    }
}
