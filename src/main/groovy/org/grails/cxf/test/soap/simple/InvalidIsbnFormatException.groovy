package org.grails.cxf.test.soap.simple

import javax.xml.bind.annotation.XmlAccessType
import javax.xml.bind.annotation.XmlAccessorType
import javax.xml.ws.WebFault

@WebFault(name="InvalidIsbnFormat")
@XmlAccessorType(XmlAccessType.FIELD)
class InvalidIsbnFormatException extends RuntimeException {
}
