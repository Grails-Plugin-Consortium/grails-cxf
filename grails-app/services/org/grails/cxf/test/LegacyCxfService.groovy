package org.grails.cxf.test

class LegacyCxfService {

    static expose = ['cxf']

    def legacyMethod(String param) {
        return "legacy ${param}"
    }
}
