package org.grails.cxf.test.soap.interceptor

import org.apache.cxf.common.injection.NoJSR250Annotations
import org.apache.cxf.ws.security.wss4j.WSS4JInInterceptor
import org.apache.cxf.phase.Phase
import java.util.logging.Logger
import org.apache.cxf.common.logging.LogUtils
import java.util.logging.Level
import java.util.logging.LogRecord
import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory
import org.apache.ws.security.WSSecurityException
import org.apache.ws.security.validate.UsernameTokenValidator
import org.apache.ws.security.WSSecurityEngine
import javax.xml.bind.Validator
import javax.xml.namespace.QName
import org.apache.ws.security.WSConstants
import org.apache.ws.security.handler.WSHandlerConstants

@NoJSR250Annotations
public class CustomSecurityInInterceptor  {

//    private static final Log log = LogFactory.getLog(CustomSecurityInInterceptor)
//
//    public CustomLoggingInInterceptor() {
//        Map<String, Object> inProps = [:]
//        inProps.put(WSHandlerConstants.ACTION, WSHandlerConstants.USERNAME_TOKEN);
//        inProps.put(WSHandlerConstants.PASSWORD_TYPE, WSConstants.PW_TEXT);
//        Map<QName, Validator> validatorMap = new HashMap<QName, Validator>();
//        validatorMap.put(WSSecurityEngine.USERNAME_TOKEN, new UsernameTokenValidator() {
//
//            @Override
//            protected void verifyPlaintextPassword(org.apache.ws.security.message.token.UsernameToken usernameToken, org.apache.ws.security.handler.RequestData data) throws org.apache.ws.security.WSSecurityException {
//                if(data.username == "wsuser" && usernameToken.password != "secret") {
//                    throw new WSSecurityException("password mismatch")
//                } else {
//                    println "user name and password were correct!"
//                }
//            }
//        });
//        inProps.put(WSS4JInInterceptor.VALIDATOR_MAP, validatorMap);
//        secureServiceFactory.getInInterceptors().add(new WSS4JInInterceptor(inProps))
//        super(inProps)
//    }


}
