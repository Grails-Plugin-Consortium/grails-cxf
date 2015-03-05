package org.grails.cxf.test.soap.simple

import org.apache.commons.validator.ISBNValidator

import javax.xml.bind.annotation.XmlAccessType
import javax.xml.bind.annotation.XmlAccessorType
import javax.xml.bind.annotation.XmlElement

@XmlAccessorType(XmlAccessType.NONE)
class Isbn {

    @XmlElement String number

    void validate() throws InvalidIsbnFormatException {
        if (!isValidIsbn(number)) {
            throw new InvalidIsbnFormatException()
        }
    }

    static Boolean isValidIsbn(String number) {
        ISBNValidator validator = new ISBNValidator()
        return validator.isValid(number)
    }

}
