package org.grails.cxf.test

import org.grails.cxf.utils.EndpointType

//this SHOULD get wired up as a service as it has the exposeAs static property
class CarService {

    static exposeAs = EndpointType.SIMPLE
    static excludes = ['dontHonk']

    String honkHorn() {
        "HONK"
    }

    String dontHonk(){
        "BEEP"
    }

    String stop(){
        "BRAKES"
    }

    String start(){
        "GAS"
    }
}
