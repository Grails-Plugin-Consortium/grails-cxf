package org.grails.cxf.test.example

import grails.transaction.Transactional
import org.grails.cxf.utils.GrailsCxfEndpoint

import javax.jws.WebMethod
import javax.jws.WebResult

@Transactional
class InterfaceService implements IInterfaceService {

	@WebMethod
	@WebResult
	String doWork() {
		return "did it"
	}
}

@GrailsCxfEndpoint
interface IInterfaceService {
	String doWork()
}
