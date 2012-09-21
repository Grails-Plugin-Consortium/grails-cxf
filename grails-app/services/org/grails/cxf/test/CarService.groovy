package org.grails.cxf.test


//this SHOULD get wired up as a service as it has the exposeAs static property
class CarService {

    static exposeAs = 'simple'

    String honkHorn() {
        "HONK"
    }
}
