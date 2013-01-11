package org.grails.cxf.test

import org.grails.cxf.utils.EndpointType
import org.grails.cxf.utils.GrailsCxfEndpoint

/**
 */
@GrailsCxfEndpoint(address = "/v2/#name", expose = EndpointType.SIMPLE, soap12 = true)
class AnnotatedSimpleAddressEndpoint {

    String simpleMethod(String param) {
        return param.toString()
    }
}
