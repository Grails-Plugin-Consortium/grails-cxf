package org.grails.cxf.test

import org.grails.cxf.utils.EndpointType
import org.grails.cxf.utils.GrailsCxfEndpoint

@GrailsCxfEndpoint(expose=EndpointType.SIMPLE)
class AnnotatedExcludesMixedCarService {

    static excludes=['dontHonk']

    String honkHorn() {
        'HONK'
    }

    String dontHonk(){
        'BEEP'
    }

    String stop(){
        'BRAKES'
    }

    String start(){
        'GAS'
    }
}
