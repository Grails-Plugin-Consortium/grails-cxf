import org.grails.cxf.test.soap.interceptor.CustomLoggingInInterceptor
import org.grails.cxf.test.soap.interceptor.InjectedBean

beans = {
    customLoggingInInterceptor(CustomLoggingInInterceptor) {
        name = "customLoggingInInterceptor"
    }

    injectedBean(InjectedBean) { bean ->
        bean.autowire = 'byName'
        name = "i was injected"
    }
}