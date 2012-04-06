package test

import grails.test.mixin.TestFor
import spock.lang.Specification
import spock.lang.Unroll

/**
 */
@TestFor(TestCxfService)
class TestCxfServiceSpec extends Specification {

    def testCxfService

    def setup() {
        testCxfService = new TestCxfService()
    }

    @Unroll
    def "test basic service methods #method"() {
        when:
        def output = testCxfService.invokeMethod(method, param)

        then:
        output == result

        where:
        method             | param   | result
        'booleanMethod'    | true    | true
        'stringMethod'     | 'hello' | 'hello'
        'sonicScrewdriver' | null    | 'buzz'
    }
}
