package org.grails.cxf.test.soap.interceptor

import org.apache.cxf.common.injection.NoJSR250Annotations

@NoJSR250Annotations
class CustomSecurityInInterceptor {

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
