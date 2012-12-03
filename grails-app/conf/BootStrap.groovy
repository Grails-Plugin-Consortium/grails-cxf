import grails.converters.JSON
import grails.converters.XML

import org.codehaus.groovy.grails.web.converters.marshaller.xml.InstanceMethodBasedMarshaller
import org.grails.cxf.test.soap.simple.SimpleException
import org.grails.cxf.utils.GrailsCxfUtils

class BootStrap {

    def grailsApplication

    def init = { servletContext ->
        GrailsCxfUtils.metaClass.getGrailsApplication = {-> grailsApplication }
        GrailsCxfUtils.metaClass.static.getGrailsApplication = {-> grailsApplication }

        JSON.registerObjectMarshaller(SimpleException) {
            [message: it.message]
        }

        XML.registerObjectMarshaller(new InstanceMethodBasedMarshaller())
    }
}
