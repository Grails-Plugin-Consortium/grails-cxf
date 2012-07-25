package test

import javax.jws.WebMethod
import javax.jws.WebParam

class TestJaxwsEndpoint implements TestService {

    static exposeAs = 'jaxws'

    @WebMethod(operationName = 'stringMethod')
    String stringMethod(@WebParam(name = 'string') String string) {
        println "recieved $string"
        string
    }

    @WebMethod(operationName = 'booleanMethod')
    Boolean booleanMethod(@WebParam(name = 'bool') Boolean bool) {
        println "recieved $bool"
        bool
    }

    String sonicScrewdriver() {
        println 'buzz'
        'buzz'
    }

}