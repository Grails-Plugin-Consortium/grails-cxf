package org.grails.cxf.test

import javax.jws.WebMethod
import javax.jws.WebResult

import org.grails.cxf.utils.EndpointType

class RemoteReportService {

    static expose = EndpointType.JAX_WS

    @WebResult(name = 'remoteReport')
    @WebMethod(operationName = 'getMeSomeReports')
    List<RemoteReport> getMeSomeReports() {
        List<RemoteReport> reports = []
        reports << RemoteReport.findOrSaveWhere(reportId: 1L, errorCode: 1L, errorMessage: 'Binders full of women.')
        reports << RemoteReport.findOrSaveWhere(reportId: 2L, errorCode: 1L, errorMessage: 'Binder?! I hardly know her!')
        reports
    }
}
