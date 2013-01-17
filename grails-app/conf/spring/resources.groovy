import org.grails.cxf.test.soap.security.CustomLoggingInInterceptor

beans = {
    customLoggingInInterceptor(CustomLoggingInInterceptor) {
        name = "customLoggingInInterceptor"
    }
}