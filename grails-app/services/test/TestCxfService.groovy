package test

class TestCxfService implements TestService {

    static expose = ['cxf']
    static exclude = ["sonicScrewdriver"]

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
