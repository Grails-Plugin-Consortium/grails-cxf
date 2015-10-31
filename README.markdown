[![Build Status](https://travis-ci.org/Grails-Plugin-Consortium/grails-cxf.png?branch=master)](https://travis-ci.org/Grails-Plugin-Consortium/grails-cxf)

<a name="Top"></a>

The docs for the 2.x branch can be found [here](https://github.com/Grails-Plugin-Consortium/grails-cxf/tree/grails-2). A lot of the previous documentation is somewhat applicable, but I will be creating new docs in the coming weeks.
  
Grails CXF Plugin
=========

The 3.x branch of the plugin is a grails plugin that contains simplified features to get simple soap endpoints exposed in grails 3 applications.

Getting Started
-----------

At the core, this plugin is a simple wrapper for geeting grails service classes wired up as direct soap endpoints.  As many of the previous features from the 2.x branch as could be ported for the initial release were ported.  There will be continued support added for the more complex CXF features going forward.
 
Basic Usage
---------

Exposing a service class is as simple as adding the `GrailsCxfEndpoint` annotation and annotating the methods you wish to expose in the service with `WebMethod` and `WebResult`

```
package com.gpc.demo

import grails.transaction.Transactional
import org.grails.cxf.utils.GrailsCxfEndpoint

import javax.jws.WebMethod
import javax.jws.WebResult

@Transactional
@GrailsCxfEndpoint
class DemoService {

    @WebMethod
    @WebResult
    String demoMethod() {
        return "demo"
    }
}
```

Returning Domain Classes
----

If you wish to return domain classes you will need to make sure to add the xml annotations to the domain class.

```
package org.grails.cxf.test.example

import grails.transaction.Transactional
import org.grails.cxf.utils.GrailsCxfEndpoint

import javax.jws.WebMethod
import javax.jws.WebParam
import javax.jws.WebResult

@Transactional
@GrailsCxfEndpoint
class PersonService {

	@WebMethod
	@WebResult(name = "Person", targetNamespace = "")
	Person createPerson(@WebParam(name = 'name') String name) {
		Person.findOrSaveByName(name)
	}
}
```

```
package org.grails.cxf.test.example

import javax.xml.bind.annotation.XmlAccessType
import javax.xml.bind.annotation.XmlAccessorType
import javax.xml.bind.annotation.XmlElement

@XmlAccessorType(XmlAccessType.NONE)
class Person {

    @XmlElement
    Long id

    @XmlElement
    String name

    static constraints = {
        name nullable: false, blank: false
    }
}
```

Custom Interceptors
-----

Do add a custom interceptor you should define an bean in your application or imported config class.  The reference to the logging interceptor is the name of the defined bean.
 
```
package org.grails.cxf.test.soap.interceptor

import org.apache.cxf.common.injection.NoJSR250Annotations
import org.apache.cxf.common.logging.LogUtils
import org.apache.cxf.interceptor.AbstractLoggingInterceptor
import org.apache.cxf.interceptor.Fault
import org.apache.cxf.message.Message
import org.apache.cxf.phase.Phase
import org.springframework.beans.factory.annotation.Autowired

import java.util.logging.Logger

/**
 */
@NoJSR250Annotations
class CustomLoggingInInterceptor extends AbstractLoggingInterceptor {

    private static final Logger LOG = LogUtils.getLogger(CustomLoggingInInterceptor)
    def name

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
```

```
package grails.cxf.demo

import grails.boot.GrailsApp
import grails.boot.config.GrailsAutoConfiguration
import org.grails.cxf.test.soap.interceptor.CustomLoggingInInterceptor
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ImportResource

class Application extends GrailsAutoConfiguration {
    static void main(String[] args) {
        GrailsApp.run(Application, args)
    }

    @Bean
    public CustomLoggingInInterceptor customLoggingInInterceptor(){
        return new CustomLoggingInInterceptor(name: 'injected value')
    }
}
```

```
package org.grails.cxf.test.example

import grails.transaction.Transactional
import org.grails.cxf.utils.GrailsCxfEndpoint

import javax.jws.WebMethod
import javax.jws.WebParam
import javax.jws.WebResult

@Transactional
@GrailsCxfEndpoint(inInterceptors = ['customLoggingInInterceptor'])
class PersonService {

	@WebMethod
	@WebResult(name = "Person", targetNamespace = "")
	Person createPerson(@WebParam(name = 'name') String name) {
		Person.findOrSaveByName(name)
	}
}
```

<p align="right"><a href="#Top">Top</a></p>
<a name="soapannotation"></a>
EXPOSING CLASSES VIA ANNOTATION
-----------------
When using the annotation, the property values will only be used if the corresponding annotation value is not provided or is set to the default value.  The following are available to configure via the annotation:

```groovy
/**
 * Annotation to be use to expose a Service or Endpoint class as a CXF Service via Grails.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@interface GrailsCxfEndpoint {
    String address() default ''
    String name() default ''
    EndpointType expose() default EndpointType.JAX_WS
    boolean soap12() default false
    String wsdl() default ''
    //Interceptors
    String[] inInterceptors() default []
    String[] outInterceptors() default []
    String[] inFaultInterceptors() default []
    String[] outFaultInterceptors() default []
    GrailsCxfEndpointProperty[] properties() default []
}

@Target(ElementType.METHOD)
@interface GrailsCxfEndpointProperty {
    public String name() default ''
    public String value() default ''
}

```

**ADDRESS**

The address property is used to adjust the endpoint address that the service will be deployed to.  By default if not provided or is the value is empty (""), this will be the name of the Service or Endpoint with the first letter lowercase and the word Endpoint or Service removed from the end of the name.  The default behavior would deploy the `BoatService` as `/services/boat`.

If you wish to override this and provide your own service name or address (for versioning support for example) you may set this value.

```groovy
@GrailsCxfEndpoint(address='/v2/custom/path')
class CarService {
    ...
}
```

The above would be deployed to `/services/v2/custom/path`.

**EXPOSE**

The `expose` property will tell the plugin how you wish to expose.  The default is `EndpointType.JAX_WS` which is the same as the following:

```groovy
@GrailsCxfEndpoint(expose=EndpointType.JAX_WS)
class CarService {
    ...
}
```

Simple and JaxRS types are currently not supported.  *TODO*

**SOAP12**

To tell a service to default to SOAP 1.2 instead of 1.1 simply set this to `true`. The default value is `false` which will use SOAP 1.1.

```groovy
@GrailsCxfEndpoint(soap12=true)
class CarService {
    ...
}
```

**WSDL**

To expose as a wsdl first jax web service endpoint <http://cxf.apache.org/docs/jax-ws-configuration.html> add the wsdl property and classpath to the wsdl as well as setting the endpoint type to `EndpointType.JAX_WS_WSDL`.

```groovy
@WebService(name = 'CustomerServiceWsdlEndpoint',
targetNamespace = 'http://test.cxf.grails.org/',
serviceName = 'CustomerServiceWsdlEndpoint',
portName = 'CustomerServiceWsdlPort')
@GrailsCxfEndpoint(wsdl = 'org/grails/cxf/test/soap/CustomerService.wsdl', expose = EndpointType.JAX_WS_WSDL)
class AnnotatedCustomerServiceWsdlEndpoint {

    CustomerServiceEndpoint customerServiceEndpoint

    List<Customer> getCustomersByName(final String name) {
        customerServiceEndpoint.getCustomersByName(name)
    }
}
```

Example *TODO* 

<a name="interceptors"></a>
**ININTERCEPTORS**

This is a list of bean names in `List<String>` to inject to the cxf service endpoint.  You will need to define your interceptor beans via normal spring dsl (in resources.groovy for example).

This is helpful when the default cxf annotation of `@org.apache.cxf.interceptor.InInterceptors (interceptors = {"com.example.Test1Interceptor" })` does not satisfy your needs.

When chosing between the this property and the cxf provided one, if you require value injection, the cxf provided annotation will most likely **NOT** meet your needs and you should use this property instead.

*Note: Make sure to set any beans you wish injected into your interceptors to `bean.autowire = 'byName'` or use the `@Autowire` annotation.*

**OUTINTERCEPTORS**

If you wish to inject a custom in interceptor bean, use this property.  This is helpful when the default cxf annotation of `@org.apache.cxf.interceptor.OutInterceptors (interceptors = {"com.example.Test1Interceptor" })` does not satisfy your needs.

See above for examples of using inInterceptor which should be very similar.

**INFAULTINTERCEPTORS**

If you wish to inject a custom in interceptor bean, use this property.  This is helpful when the default cxf annotation of `@org.apache.cxf.interceptor.InFaultInterceptors (interceptors = {"com.example.Test1Interceptor" })` does not satisfy your needs.

See above for examples of using inInterceptor which should be very similar.

**OUTFAULTINTERCEPTORS**

If you wish to inject a custom in interceptor bean, use this property.  This is helpful when the default cxf annotation of `@org.apache.cxf.interceptor.OutFaultInterceptors (interceptors = {"com.example.Test1Interceptor" })` does not satisfy your needs.

See above for examples of using inInterceptor which should be very similar.

**CONCLUSION**

Using the annotation will help reduce the clutter of having many static properties in your class to configure cxf.


Demo Project
---------
https://github.com/Grails-Plugin-Consortium/grails-cxf-demo




<p align="right"><a href="#Top">Top</a></p>
<a name="License"></a>
LICENSE
---------------

Copyright 2013 Christian Oestreich

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
