package org.grails.cxf.test

import javax.xml.bind.annotation.XmlAccessType
import javax.xml.bind.annotation.XmlAccessorType
import javax.xml.bind.annotation.XmlAttribute

@XmlAccessorType(XmlAccessType.NONE)
class Word implements Serializable { //, Comparable<Word> {

    static belongsTo = [page: Page]

    @XmlAttribute
    String text

    //If this is a SortedSet in the parent class
//    int compareTo(Word o) {
//        this.text <=> o.text
//    }
}
