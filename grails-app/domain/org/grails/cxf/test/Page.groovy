package org.grails.cxf.test

import javax.xml.bind.annotation.*

@XmlAccessorType(XmlAccessType.NONE)
class Page implements Serializable {
    @XmlElement
    String name
    @XmlElement
    Integer number

    static mapping = {
        version true
    }
}