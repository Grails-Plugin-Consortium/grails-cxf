import org.grails.cxf.utils.GrailsCxfUtils

class BootStrap {

    def grailsApplication

    def init = { servletContext ->
        GrailsCxfUtils.class.metaClass.getGrailsApplication = {-> grailsApplication }
        GrailsCxfUtils.class.metaClass.static.getGrailsApplication = {-> grailsApplication }
    }

    def destroy = {
    }
}
