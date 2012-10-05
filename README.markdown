<a name="Top"></a>

CXF CLIENT
======

* <a href="#Introduction">Introduction</a>
* <a href="#Script">Wsdl2java Script</a>
* <a href="#Plugin">Plugin Configuration</a>
* <a href="#wsdl">Wsdl First Services</a>
* <a href="#maps">Handling Map Responses</a>
* <a href="#Mime">Mime Attachments</a>
* <a href="#Security">Custom Security Interceptors</a>
* <a href="#In">Custom In Interceptors</a>
* <a href="#Out">Custom Out Interceptors</a>
* <a href="#Fault">Custom Out Fault Interceptors</a>
* <a href="#Custom">Custom Http Client Policy</a>
* <a href="#Exceptions">Dealing With Exceptions</a>
* <a href="#Beans">User Client Beans Anywhere</a>
* <a href="#Endpoints">Retrieving and Updating Endpoints</a>
* <a href="#Demo">Demo Project</a>
* <a href="#Issues">Issues</a>
* <a href="#Change">Change Log</a>
* <a href="#Future">Future Revisions</a>
* <a href="#License">License</a>

<a name="Introduction"></a>
INTRODUCTION
---------------

The Grails Cxf plugin makes exposing services as SOAP endpoints easy and painless.  Since version 1.0.0, it was rewritten and enhanced to support more features including the migration to grails 2.0+.

Some new things as of version 1.0.0 are as follows:

* The suggested pattern to separate cxf endpoints it to have endpoints live in grails-app\endpoints directory instead of grails-app\services
* The plugin will autowire configured classes in the grails-app\endpoints\** AND the grails-app\services\** directories
* Endpoint creation scripts create-endpoint, create-endpoint-simple scripts will create cxf artefacts in grails-app\endpoints
* Service creation scripts create-cxf-service, create-cxf-service-simple will create cxf artefacts in grails-app\services
* Built in support for simple Map response type handling via `@XmlJavaTypeAdapter(GrailsCxfMapAdapter.class)` method annotation <a href="#maps">example below</a>
* Many new examples to help with configuration can be found in the source via functional specs and test classes at <https://github.com/thorstadt/grails-cxf>
* Default plugin configuration is provided via `DefaultCxfConfig.groovy`.  Although unlikely, you can override in your projects Config.groovy
* The default url for wsdl viewing remains `http://.../[app name if not root]/services` as it was in previous versions.  Multiple cxf servlet endpoints can be configured or the default changed via Config.goovy
* Wsdl First services are now available to use <a href="#wsdl">example below</a>

<p align="right"><a href="#Top">Top</a></p>
<a name="Script"></a>
WSDL2JAVA SCRIPT
---------------
Included with the plugin is a convenient script to generate java code from a wsdl.  Please note that the grails cxf-client also includes a similar script albeit using different configuration to create java files from Config.groovy instead of command line params.

```
usage: grails wsdl2-java --wsdl=<path to wsdl> [--package=<package>]

Script Options:
  -h, --help           Prints this help message
  -p, --package=arg    The package to put the generated Java objects in.
  -w, --wsdl=arg       The path to the wsdl to use.
```

See <http://cxf.apache.org/docs/wsdl-to-java.html> for additional options.

<p align="right"><a href="#Top">Top</a></p>
<a name="Plugin"></a>
PLUGIN CONFIGURATION
-----------------
There are many ways to configure the plugin.  The most straight forward way is to use the `static expose = ['cxf']`  in your service class.  Legacy support for both cxf and cxf-jax type services remains, but the new preferred way is to use one of the following methods of exposure:

To expose as a simple endpoint <http://cxf.apache.org/docs/simple-frontend-configuration.html> add the following to your endpoint or service class:

```groovy
    static expose = EndpointType.SIMPLE
```

To expose as a jax web service endpoint <http://cxf.apache.org/docs/jax-ws-configuration.html> add the following to your endpoint or service class:

```groovy
    static expose = EndpointType.JAX_WS
```

To expose as a wsdl first jax web service endpoint <http://cxf.apache.org/docs/jax-ws-configuration.html> add the following to your endpoint or service class:

```groovy
     static expose = EndpointType.JAX_WS_WSDL
     static wsdl = 'org/grails/cxf/test/soap/CustomerService.wsdl' //your path (preferred) or url to wsdl
```

To expose as a jax rest service endpoint <http://cxf.apache.org/docs/jax-rs.html> add the following to your endpoint or service class:

```groovy
     static expose = EndpointType.JAX_RS
```

Please note that while possible to use the string literals behond the `EndpointType` constants, using the constant is much preferred and will cause less issues with spellings and upgrade issues in the future.


<p align="right"><a href="#Top">Top</a></p>
<a name="wsdl"></a>
WSDL FIRST SERVICES
-----------------
You can now configure cxf services to look at wsdl's for endpoint generation by adding the following to the service or endpoint

```groovy
    static expose = EndpointType.JAX_WS_WSDL
    static wsdl = 'org/grails/cxf/test/soap/CustomerService.wsdl'
```

A complete example is below:

```groovy
import javax.jws.WebService
import org.grails.cxf.utils.EndpointType

@WebService(name = 'CustomerServiceWsdlEndpoint',
targetNamespace = 'http://test.cxf.grails.org/',
serviceName = 'CustomerServiceWsdlEndpoint',
portName = 'CustomerServiceWsdlPort')
class CustomerServiceWsdlEndpoint {

    static expose = EndpointType.JAX_WS_WSDL
    static wsdl = 'org/grails/cxf/test/soap/CustomerService.wsdl'

    List<Customer> getCustomersByName(final String name) {
        ... do work ...
    }
}
```


<p align="right"><a href="#Top">Top</a></p>
<a name="maps"></a>
HANDLING MAP RESPONSES
-----------------
Provided with the plugin is a default `XmlJavaTypeAdapter` to help handle map types.  You can write your own if you wish to change the name or object type support.  Using the file `GrailsCxfMapAdapter.groovy` as a baseline will help get you started should you need more complex support than is provided.  Please note that the class `GrailsCxfMapAdapter` will use the toString() for the keys and values in the map for the response.  If you want to use specific object property types that are compound or don't support toString() you will need to create your own adapter.

```groovy
class CoffeeMakerEndpoint {

    static expose = EndpointType.SIMPLE

    @XmlJavaTypeAdapter(GrailsCxfMapAdapter.class)
    @WebResult(name='entires')
    Map<String, CoffeeType> mapCoffeeLocations() {
        return ['Colombia': CoffeeType.Colombian, 'Ethiopia': CoffeeType.Ethiopian]
    }
}
```

The Response will look similar to the following (note the nodes `key` and `value` are auto created for you)
```xml
<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
   <soap:Body>
      <ns1:mapCoffeeLocationsResponse xmlns:ns1="http://test.cxf.grails.org/">
         <return>
            <entry>
               <key xsi:type="xs:string" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">Colombia</key>
               <value xsi:type="xs:string" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">Colombian</value>
            </entry>
            <entry>
               <key xsi:type="xs:string" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">Ethiopia</key>
               <value xsi:type="xs:string" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">Ethiopian</value>
            </entry>
         </return>
      </ns1:mapCoffeeLocationsResponse>
   </soap:Body>
</soap:Envelope>
```








<p align="right"><a href="#Top">Top</a></p>
<a name="Future"></a>
FUTURE REVISIONS
---------------

Currently taking submissions for improvements.


<p align="right"><a href="#Top">Top</a></p>
<a name="License"></a>
LICENSE
---------------

Copyright 2012 Christian Oestreich

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.