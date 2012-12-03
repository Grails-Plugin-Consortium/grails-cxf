package org.grails.cxf.test

import javax.xml.bind.annotation.XmlAccessType
import javax.xml.bind.annotation.XmlAccessorType
import javax.xml.bind.annotation.XmlElement
import javax.xml.bind.annotation.XmlElementWrapper

@XmlAccessorType(XmlAccessType.NONE)
class Page implements Serializable {
    @XmlElement
    Long id
    @XmlElement
    String name
    @XmlElement
    Integer number
    @XmlElement
    Long version

    static hasMany = [words: Word]

    @XmlElementWrapper(name="words")
    @XmlElement(name="word")
    List<Word> words
}
