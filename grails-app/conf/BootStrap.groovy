import grails.converters.JSON
import grails.converters.XML

import org.codehaus.groovy.grails.web.converters.marshaller.xml.InstanceMethodBasedMarshaller
import org.grails.cxf.test.soap.simple.SimpleException
import org.grails.cxf.utils.GrailsCxfUtils
import org.apache.ws.security.handler.WSHandlerConstants
import org.apache.ws.security.WSConstants
import javax.xml.namespace.QName
import org.apache.ws.security.validate.Validator
import org.apache.ws.security.WSSecurityEngine
import org.apache.ws.security.validate.UsernameTokenValidator
import org.apache.ws.security.WSSecurityException
import org.apache.cxf.ws.security.wss4j.WSS4JInInterceptor
import org.apache.cxf.frontend.ServerFactoryBean

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
        inProps.put(WSHandlerConstants.PASSWORD_TYPE, WSConstants.PW_TEXT);
        Map<QName, Validator> validatorMap = new HashMap<QName, Validator>();
        validatorMap.put(WSSecurityEngine.USERNAME_TOKEN, new UsernameTokenValidator() {

            @Override
            protected void verifyPlaintextPassword(org.apache.ws.security.message.token.UsernameToken usernameToken, org.apache.ws.security.handler.RequestData data) throws org.apache.ws.security.WSSecurityException {
                if(data.username == "wsuser" && usernameToken.password != "secret") {
                    throw new WSSecurityException("password mismatch")
                } else {
                    println "user name and password were correct!"
                }
            }
        });
        inProps.put(WSS4JInInterceptor.VALIDATOR_MAP, validatorMap);
        annotatedSecureServiceFactory.getInInterceptors().add(new WSS4JInInterceptor(inProps))
//        annotatedSecureServiceFactory.getProperties(true).put("ws-security.enable.nonce.cache","false")
//        annotatedSecureServiceFactory.getProperties(true).put("ws-security.enable.timestamp.cache","false")
    }
}
