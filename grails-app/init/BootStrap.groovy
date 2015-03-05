import grails.converters.JSON
import grails.converters.XML
import org.apache.cxf.frontend.ServerFactoryBean
import org.apache.cxf.ws.security.wss4j.WSS4JInInterceptor
import org.apache.ws.security.WSConstants
import org.apache.ws.security.handler.WSHandlerConstants
import org.apache.wss4j.common.ext.WSPasswordCallback
import org.grails.cxf.test.soap.simple.SimpleException
import org.grails.cxf.utils.GrailsCxfUtils
import org.grails.web.converters.marshaller.xml.InstanceMethodBasedMarshaller

import javax.security.auth.callback.Callback
import javax.security.auth.callback.CallbackHandler
import javax.security.auth.callback.UnsupportedCallbackException

class BootStrap {

    def grailsApplication
    ServerFactoryBean annotatedSecureServiceFactory

    def init = { servletContext ->
        GrailsCxfUtils.metaClass.getGrailsApplication = { -> grailsApplication }
        GrailsCxfUtils.metaClass.static.getGrailsApplication = { -> grailsApplication }

        JSON.registerObjectMarshaller(SimpleException) {
            [message: it.message]
        }

        XML.registerObjectMarshaller(new InstanceMethodBasedMarshaller())

//        //Register some wss4j security
        Map<String, Object> inProps = [:]

        inProps.put(WSHandlerConstants.ACTION, WSHandlerConstants.USERNAME_TOKEN);
// Password type : plain text
//        inProps.put(WSHandlerConstants.PASSWORD_TYPE, WSConstants.PW_TEXT);
// for hashed password use:
        properties.put(WSHandlerConstants.PASSWORD_TYPE, WSConstants.PW_DIGEST);
// Callback used to retrieve password for given user.
        inProps.put(WSHandlerConstants.PW_CALLBACK_CLASS, ServerPasswordHandler.class.getName());

        annotatedSecureServiceFactory.getInInterceptors().add(new WSS4JInInterceptor(inProps))
//        annotatedSecureServiceFactory.getProperties(true).put("ws-security.enable.nonce.cache","false")
//        annotatedSecureServiceFactory.getProperties(true).put("ws-security.enable.timestamp.cache","false")
    }
}

class ServerPasswordHandler implements CallbackHandler {
    public void handle(Callback[] callbacks) throws IOException,
            UnsupportedCallbackException {
        WSPasswordCallback pc = (WSPasswordCallback) callbacks[0];

        if ("wsuser".equals(pc.getIdentifier())) {
            pc.setPassword("secret");
        }
    }
}

