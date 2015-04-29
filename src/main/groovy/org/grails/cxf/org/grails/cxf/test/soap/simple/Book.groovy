package org.grails.cxf.test.soap.simple

import javax.xml.bind.annotation.XmlAccessType
import javax.xml.bind.annotation.XmlAccessorType

@XmlAccessorType(XmlAccessType.FIELD)
class Book {
    String title
    List<String> authors
    Isbn isbn
}
