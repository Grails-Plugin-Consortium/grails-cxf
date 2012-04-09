package test

import grails.test.mixin.TestFor
import spock.lang.Specification
import spock.lang.Unroll

/**
 */
@TestFor(TestCxfJaxService)
class TestCxfJaxServiceSpec extends Specification {

  def testCxfJaxService

  def setup() {
    testCxfJaxService = new TestCxfJaxService()
  }

  @Unroll
  def "test basic service methods #method"() {
    when:
    def output = testCxfJaxService.invokeMethod(method, param)

    then:
    output == result

    where:
    method             | param   | result
    'booleanMethod'    | true    | true
    'stringMethod'     | 'hello' | 'hello'
    'sonicScrewdriver' | null    | 'buzz'
  }
}
