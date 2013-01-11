package org.grails.cxf.test

import org.grails.cxf.utils.EndpointType
import org.grails.cxf.utils.GrailsCxfEndpoint

@GrailsCxfEndpoint(expose=EndpointType.SIMPLE, excludes=['dontHonk'])
class AnnotatedExcludesCarService {

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
