import org.grails.cxf.test.soap.security.CustomLoggingInInterceptor
import org.grails.cxf.test.soap.security.InjectedBean

beans = {
    customLoggingInInterceptor(CustomLoggingInInterceptor) {
        name = "customLoggingInInterceptor"
    }

    injectedBean(InjectedBean) { bean ->
        bean.autowire = 'byName'
        name = "i was injected"
    }
}