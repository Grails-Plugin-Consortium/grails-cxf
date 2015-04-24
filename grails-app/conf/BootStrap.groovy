import grails.converters.JSON
import grails.converters.XML
import org.apache.cxf.frontend.ServerFactoryBean
import org.apache.cxf.ws.security.wss4j.WSS4JInInterceptor
import org.apache.wss4j.common.ext.WSPasswordCallback
import org.apache.wss4j.dom.WSConstants
import org.apache.wss4j.dom.handler.WSHandlerConstants
import org.codehaus.groovy.grails.web.converters.marshaller.xml.InstanceMethodBasedMarshaller
import org.grails.cxf.test.soap.simple.SimpleException
import org.grails.cxf.utils.GrailsCxfUtils

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

        Map<String, Object> inProps = [:]
        inProps.put(WSHandlerConstants.ACTION, WSHandlerConstants.USERNAME_TOKEN);
        inProps.put(WSHandlerConstants.PASSWORD_TYPE, WSConstants.PW_TEXT);
        inProps.put(WSHandlerConstants.PW_CALLBACK_REF, new UsernamePasswordCallbackHandler());

        annotatedSecureServiceFactory.inInterceptors.add(new WSS4JInInterceptor(inProps))
        annotatedSecureServiceFactory.getProperties(true).put("ws-security.enable.nonce.cache", "false")
        annotatedSecureServiceFactory.getProperties(true).put("ws-security.enable.timestamp.cache", "false")
    }
}

public class UsernamePasswordCallbackHandler implements CallbackHandler {

    private Map<String, String> users = new HashMap<String, String>();

    public UsernamePasswordCallbackHandler() {
        users.put("wsuser", "password");
        users.put("bob", "security");
        users.put("alice", "securityPassword");
    }

    public void handle(Callback[] callbacks)
            throws IOException, UnsupportedCallbackException {
        for (int i = 0; i < callbacks.length; i++) {
            if (callbacks[i] instanceof WSPasswordCallback) {
                WSPasswordCallback pc = (WSPasswordCallback) callbacks[i];
                if (pc.getUsage() == WSPasswordCallback.USERNAME_TOKEN) {
                    pc.setPassword(users.get(pc.getIdentifier()));
                    break;
                }
            } else {
                throw new UnsupportedCallbackException(callbacks[i], "Unrecognized Callback");
            }
        }
    }
}