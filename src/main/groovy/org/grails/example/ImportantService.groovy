package org.grails.example

import javax.jws.WebMethod
import javax.jws.WebParam
import javax.jws.WebService

class ImportantServiceImpl implements ImportantService {
    String getImportantData(String type) {
        return "type=$type"
    }
}

@WebService(serviceName = 'ImportantService')
interface ImportantService {
    @WebMethod()
    String getImportantData(@WebParam(name = 'type') String type)
}
