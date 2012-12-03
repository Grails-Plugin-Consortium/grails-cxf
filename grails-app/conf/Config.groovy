log4j = {

    if(System.getProperty('showFullStackTrace')) {
        error stdout: "StackTrace"
    }

    error 'org.codehaus.groovy.grails',
          'org.springframework',
          'org.hibernate',
          'net.sf.ehcache.hibernate'

    trace 'org.grails.cxf',
          'org.grails.cxf.artefact',
          'org.grails.cxf.frontend',
          'org.grails.cxf.servlet',
          'org.grails.cxf.utils'
}
