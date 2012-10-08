package org.grails.cxf.test


//this should NOT get wired up as a service as it is missing the expose static property
class TruckService {
    String crushCars() {
        'CRUNCH'
    }
}
