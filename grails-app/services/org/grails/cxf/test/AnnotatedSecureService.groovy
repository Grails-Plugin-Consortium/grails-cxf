package org.grails.cxf.test

import org.grails.cxf.utils.EndpointType
import org.grails.cxf.utils.GrailsCxfEndpoint
import org.grails.cxf.utils.GrailsCxfEndpointProperty

import javax.jws.WebMethod
import javax.jws.WebParam
import javax.jws.WebResult

@GrailsCxfEndpoint(expose = EndpointType.JAX_WS,properties = [@GrailsCxfEndpointProperty(name = "ws-security.enable.nonce.cache", value = "false"), @GrailsCxfEndpointProperty(name = "ws-security.enable.timestamp.cache", value = "false")])
class AnnotatedSecureService {

    @WebMethod(operationName = "simpleMethod")
    @WebResult(name = "simpleResult")
    String simpleMethod(@WebParam(name = "param") String param) {
        return param.toString()
    }
}
