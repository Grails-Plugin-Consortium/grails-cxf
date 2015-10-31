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
`
<p align="right"><a href="#Top">Top</a></p>
<a name="License"></a>
LICENSE
---------------

Copyright 2013 Christian Oestreich

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
