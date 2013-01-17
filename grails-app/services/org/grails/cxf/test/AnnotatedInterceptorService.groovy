package org.grails.cxf.test

import org.grails.cxf.utils.GrailsCxfEndpoint

import javax.jws.WebMethod
import javax.jws.WebParam
import javax.jws.WebResult
import org.apache.cxf.interceptor.InInterceptors

/**
 */
@GrailsCxfEndpoint(inInterceptors = ["customLoggingInInterceptor"])
class AnnotatedInterceptorService {

    @WebMethod(operationName="simpleMethod")
    @WebResult(name="simpleResult")
    String simpleMethod(@WebParam(name="param") String param) {
        return param.toString()
    }
}
