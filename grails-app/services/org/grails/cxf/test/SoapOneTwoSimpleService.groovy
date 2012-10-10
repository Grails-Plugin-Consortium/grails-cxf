package org.grails.cxf.test

import org.grails.cxf.utils.EndpointType

class SoapOneTwoSimpleService {

    static expose = [EndpointType.SIMPLE]
    static soap12 = true

    String simpleMethod(String param) {
        return param.toString()
    }
}
