package org.grails.cxf.test.example

import grails.transaction.Transactional
import org.grails.cxf.utils.GrailsCxfEndpoint

import javax.jws.WebMethod
import javax.jws.WebParam
import javax.jws.WebResult

@GrailsCxfEndpoint()
@Transactional
class ImportantService {

	@WebMethod
	@WebResult(name = "ImportantData", targetNamespace = "")
	String getImportantData(@WebParam(name = 'type') String type) {
		return "type=$type"
	}
}
