package org.grails.cxf.test.soap.security

import org.apache.cxf.common.injection.NoJSR250Annotations
import org.apache.cxf.common.logging.LogUtils
import org.apache.cxf.interceptor.AbstractLoggingInterceptor
import org.apache.cxf.interceptor.Fault
import org.apache.cxf.message.Message
import org.apache.cxf.phase.Phase
import java.util.logging.Logger

/**
 */
@NoJSR250Annotations
public class CustomLoggingInInterceptor extends AbstractLoggingInterceptor {

    private static final Logger LOG = LogUtils.getLogger(CustomLoggingInInterceptor)
    def name

    public CustomLoggingInInterceptor() {
        super(Phase.RECEIVE);
        log LOG, "Creating the custom interceptor bean"
    }

    public void handleMessage(Message message) throws Fault {
        //get another web service bean here by name and call it
        log LOG, "$name :: I AM IN CUSTOM IN LOGGER!!!!!!!"
    }

    @Override
    protected Logger getLogger() {
        LOG
    }
}