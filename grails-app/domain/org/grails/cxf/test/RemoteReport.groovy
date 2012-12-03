package org.grails.cxf.test

import javax.xml.bind.annotation.XmlAccessType
import javax.xml.bind.annotation.XmlAccessorType
import javax.xml.bind.annotation.XmlElement
import javax.xml.bind.annotation.XmlRootElement
import javax.xml.bind.annotation.XmlType

@XmlRootElement(name='remoteReport')
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(propOrder = ['errorCode','errorMessage','reportId'])
class RemoteReport {
    @XmlElement
    Long errorCode

    @XmlElement
    String errorMessage

    @XmlElement
    Long reportId
}
