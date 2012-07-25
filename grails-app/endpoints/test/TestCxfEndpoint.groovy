package test

class TestCxfEndpoint implements TestService {

    static exposeAs = 'cxf'
    static excludes = ["sonicScrewdriver"]

    String stringMethod(String string) {
        println "recieved $string"
        string
    }

    Boolean booleanMethod(Boolean bool) {
        println "recieved $bool"
        bool
    }

    String sonicScrewdriver() {
        println 'buzz'
        'buzz'
    }

}
