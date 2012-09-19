package org.grails.cxf.utils

import spock.lang.Specification

class NavigableConfigurationSpec extends Specification {

    def simpleConfig

    def setup() {
        simpleConfig = new ConfigSlurper().parse("""
                apple.pear.cherry = 'tree'

                apple {
                    pear {
                        peach = 'yum'
                    }
                }

                strawberry {
                    blackberry {
                        blueberry.rassberry = 4
                    }
                }
            """)
    }

    def "get configuration at path"() {
        when:
        def found = new NavigableConfiguration(simpleConfig).get(configPath)

        then:
        found == expected

        where:
        configPath                                  | expected
        'apple.pear.cherry'                         | 'tree'
        'apple.pear.peach'                          | 'yum'
        'strawberry.blackberry.blueberry.rassberry' | 4
        'fruit.config.not.found'                    | new ConfigObject()
    }

    def "set configuration the normal way"() {
        when:
        simpleConfig.apple.pear.cherry = 'abc'

        then:
        'abc' == simpleConfig.apple.pear.cherry
    }

    def "set configuration at path"() {
        when:
        new NavigableConfiguration(simpleConfig).set(configPath, value)

        then:
        new NavigableConfiguration(simpleConfig).get(configPath) == value

        where:
        configPath                                  | value
        'apple.pear.cherry'                         | 'fruit'
        'apple.pear.peach'                          | 'moar'
        'strawberry.blackberry.blueberry.rassberry' | 'berry'
        'fruit.wasnt.here'                          | 'awesome'
    }

}
