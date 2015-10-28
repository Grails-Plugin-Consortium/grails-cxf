package org.grails.example

import javax.xml.bind.annotation.XmlAccessType
import javax.xml.bind.annotation.XmlAccessorType
import javax.xml.bind.annotation.XmlElement

@XmlAccessorType(XmlAccessType.NONE)
class Person {

    @XmlElement
    Long id

    @XmlElement
    String name

    static constraints = {
        name nullable: false, blank: false
    }
}
