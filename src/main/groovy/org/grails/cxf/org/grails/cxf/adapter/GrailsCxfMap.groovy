package org.grails.cxf.adapter

import javax.xml.bind.annotation.XmlAccessType
import javax.xml.bind.annotation.XmlAccessorType
import javax.xml.bind.annotation.XmlType
import javax.xml.bind.annotation.XmlElement

@XmlType(name = 'GrailsCxfMap')
@XmlAccessorType(XmlAccessType.FIELD)
class GrailsCxfMap {
    @XmlElement(nillable = false, name = 'entry')
    List<KeyValueEntry> entries = []

    List getEntries() {
        entries
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = 'KeyValue')
    static class KeyValueEntry {
        //Map keys cannot be null
        @XmlElement(required = true, nillable = false)
        def key
        def value
    }
}
