package org.grails.cxf.test

import org.grails.cxf.utils.EndpointType

import javax.jws.WebMethod
import javax.jws.WebResult

class PageService {

    static expose = [EndpointType.JAX_WS]

    @WebResult(name = 'page')
    @WebMethod(operationName = 'getMeSomePages')
    List<Page> getMeSomePages() {
        List<Page> pages = []
        pages << new Page(name: "test1", number: 2, version: 1)
        pages << new Page(name: "hihi", number: 8, version: 1)
        pages << new Page(name: "hoho", number: 32, version: 1)
        pages
    }
}
