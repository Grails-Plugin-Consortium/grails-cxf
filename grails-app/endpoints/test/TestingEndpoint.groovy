package test

/**
 *
 */
class TestingEndpoint {
    static exposeAs = 'CXF'
    static excludes = ['methodTwo']
    static servletName = 'Foobar'

    String methodOne() {
        return "one"
    }

    String methodTwo() {
        return "two"
    }
}
