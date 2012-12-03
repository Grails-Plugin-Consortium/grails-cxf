package org.grails.cxf.test

import javax.xml.bind.annotation.XmlAccessType
import javax.xml.bind.annotation.XmlAccessorType
import javax.xml.bind.annotation.XmlAttribute
import javax.xml.bind.annotation.XmlElement

@XmlAccessorType(XmlAccessType.NONE)
class Chapter {

    @XmlAttribute
    String name

    static hasMany = [pages: Page]

    @XmlElement
    List<Page> pages
}
