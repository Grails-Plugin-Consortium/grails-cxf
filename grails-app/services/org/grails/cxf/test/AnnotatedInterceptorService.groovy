package org.grails.cxf.test

import org.apache.cxf.interceptor.InInterceptors
import org.grails.cxf.test.soap.interceptor.CustomLoggingInInterceptor
import org.grails.cxf.utils.GrailsCxfEndpoint

import javax.jws.WebMethod
import javax.jws.WebParam
import javax.jws.WebResult

/**
 */
//this works
@GrailsCxfEndpoint(inInterceptors = ["customLoggingInInterceptor"])
//@GrailsCxfEndpoint()
//these seem to fail injection of any internal beans
@InInterceptors(classes = [CustomLoggingInInterceptor])
//@InInterceptors(interceptors=['CustomLoggingInInterceptor'])
class AnnotatedInterceptorService {

    @WebMethod(operationName = "simpleMethod")
    @WebResult(name = "simpleResult")
    String simpleMethod(@WebParam(name = "param") String param) {
        return param.toString()
    }
}
