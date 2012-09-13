package org.grails.cxf.test

import org.grails.cxf.test.soap.simple.Book
import org.grails.cxf.test.soap.simple.InvalidIsbnFormatException
import org.grails.cxf.test.soap.simple.Isbn

import javax.jws.WebMethod
import javax.jws.WebParam
import javax.jws.WebResult
import javax.jws.WebService

@WebService
interface BookStoreService {

    @WebResult(name='book')
    @WebMethod Book findBookByIsbnNumber(
            @WebParam(name="number") String number
    ) throws InvalidIsbnFormatException

    @WebResult(name='book')
    @WebMethod Book findBookByIsbn(
            @WebParam(name="isbn") Isbn isbn
    ) throws InvalidIsbnFormatException

}