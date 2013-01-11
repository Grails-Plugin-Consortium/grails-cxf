package org.grails.cxf.utils

import java.lang.annotation.RetentionPolicy
import java.lang.annotation.Retention
import java.lang.annotation.ElementType
import java.lang.annotation.Target

/**
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface GrailsCxfEndpoint {
    //address="/v2/#name", name="SimpleOneTwo", expose=[EndpointType.SIMPLE], soap12=true
    String address() default ""
    String name() default ""
    EndpointType expose() default EndpointType.JAX_WS
    boolean soap12() default false
    String wsdl() default ""
    String[] excludes() default []
}
