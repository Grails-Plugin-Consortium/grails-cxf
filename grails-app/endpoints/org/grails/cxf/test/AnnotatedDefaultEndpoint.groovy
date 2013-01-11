package org.grails.cxf.test

import org.grails.cxf.utils.GrailsCxfEndpoint
import javax.jws.WebMethod
import javax.jws.WebParam
import javax.jws.WebResult

/**
 */
@GrailsCxfEndpoint()
class AnnotatedDefaultEndpoint {

    @WebMethod
    @WebResult(name='simpleResult')
    String simpleMethod(@WebParam(name='param') String param) {
        return param.toString()
    }
}
