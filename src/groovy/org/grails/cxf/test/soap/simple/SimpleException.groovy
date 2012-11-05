package org.grails.cxf.test.soap.simple

import javax.xml.bind.annotation.XmlAccessType
import javax.xml.bind.annotation.XmlAccessorType
import javax.xml.bind.annotation.XmlElement

/**
 */
@XmlAccessorType(XmlAccessType.NONE)
class SimpleException extends Exception {

    @XmlElement
    String message

    SimpleException(String message) {
        super(message)
    }
}
