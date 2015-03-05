package org.grails.cxf.test.soap.interceptor

import org.apache.cxf.common.injection.NoJSR250Annotations
import org.apache.cxf.common.logging.LogUtils
import org.apache.cxf.interceptor.AbstractLoggingInterceptor
import org.apache.cxf.interceptor.Fault
import org.apache.cxf.message.Message
import org.apache.cxf.phase.Phase
import org.springframework.stereotype.Component

import java.util.logging.Logger
import javax.annotation.Resource

/**
 */
@NoJSR250Annotations
@Component
class CustomLoggingInInterceptor extends AbstractLoggingInterceptor {

    private static final Logger LOG = LogUtils.getLogger(CustomLoggingInInterceptor)
    def name
//    @Autowired //or set bean spring dsl to bean.autowire = "byName"
    @Resource(name = "injectedBean")
    InjectedBean injectedBean

    CustomLoggingInInterceptor() {
        super(Phase.RECEIVE)
        log LOG, 'Creating the custom interceptor bean'
    }

    void handleMessage(Message message) throws Fault {
        //get another web service bean here by name and call it

        //Check to see if cxf annotations will inject the bean (looks like no!)
        log LOG, injectedBean?.name ?: 'FAIL - NOT SET'
        log LOG, "$name :: I AM IN CUSTOM IN LOGGER!!!!!!!"
    }

    @Override
    protected Logger getLogger() {
        LOG
    }
}