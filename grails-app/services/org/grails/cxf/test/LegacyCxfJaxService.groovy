package org.grails.cxf.test

import javax.jws.WebMethod
import javax.jws.WebParam
import javax.jws.WebResult

class LegacyCxfJaxService {

    static expose = ['cxfjax']

    @WebResult(name="legacyResult")
    @WebMethod(operationName="legacyMethod")
    def legacyMethod(@WebParam(name="param") String param) {
        return "legacy ${param}"
    }

    def hiddenMethod(String param){
        return "hidden ${param}"
    }
}
