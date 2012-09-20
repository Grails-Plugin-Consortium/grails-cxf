package org.grails.cxf.test

class CarService {

    static exposeAs = 'simple'

    String honkHorn() {
        "HONK"
    }
}
