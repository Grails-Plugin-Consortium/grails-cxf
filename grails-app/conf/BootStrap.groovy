import org.grails.cxf.utils.GrailsCxfUtils
import grails.converters.JSON
import grails.converters.XML
import org.grails.cxf.test.soap.simple.SimpleException

class BootStrap {

    def grailsApplication

    def init = { servletContext ->
        GrailsCxfUtils.class.metaClass.getGrailsApplication = {-> grailsApplication }
        GrailsCxfUtils.class.metaClass.static.getGrailsApplication = {-> grailsApplication }

        JSON.registerObjectMarshaller(SimpleException) {
            def returnArray = [:]
            returnArray['message'] = it.message
            return returnArray
        }

        XML.registerObjectMarshaller(new org.codehaus.groovy.grails.web.converters.marshaller.xml.InstanceMethodBasedMarshaller())
    }

    def destroy = {
    }
}
