package org.grails.cxf.test

import org.grails.cxf.utils.GrailsCxfEndpoint
import org.grails.cxf.utils.EndpointType

/**
 */
@GrailsCxfEndpoint(address = "/v2/renamed", expose = EndpointType.SIMPLE, soap12 = true)
class AnnotatedSimpleEndpoint {

    String simpleMethod(String param) {
        return param.toString()
    }
}
