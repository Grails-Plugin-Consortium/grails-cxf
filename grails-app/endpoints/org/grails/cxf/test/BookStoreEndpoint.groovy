package org.grails.cxf.test

import org.grails.cxf.test.soap.simple.Book
import org.grails.cxf.test.soap.simple.InvalidIsbnFormatException
import org.grails.cxf.test.soap.simple.Isbn

/**
 * An example of a Code First service using JAXB annotated objects and and Cxf Reflection service generation which will
 * make use of the JAX-WS annotations on the interface.
 */
class BookStoreEndpoint implements BookStoreService {

    static excludes = ['validateIsbnAndReturnBook']

    Book findBookByIsbnNumber(final String number) throws InvalidIsbnFormatException {
        Isbn isbn = new Isbn(number: number)
        return validateIsbnAndReturnBook(isbn)
    }

    Book findBookByIsbn(final Isbn isbn) throws InvalidIsbnFormatException {
        return validateIsbnAndReturnBook(isbn)
    }

    Book validateIsbnAndReturnBook(final Isbn isbn) {
        isbn.validate()

        return new Book(title: 'The Definitive Book of Awesomeness',
                authors: ['The Definitive Author of Awesomeness', 'Bob'],
                isbn: isbn)
    }
}
