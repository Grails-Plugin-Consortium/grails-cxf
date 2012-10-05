<a name="Top"></a>

CXF CLIENT
======

* <a href="#Introduction">Introduction</a>
* <a href="#Script">Wsdl2java Script</a>
* <a href="#plugin">Plugin Configuration</a>
* <a href="#soap">Exposing Classes via SOAP</a>
* <a href="#soap12">Using SOAP 1.2</a>
* <a href="#wsdl">Wsdl First Services</a>
* <a href="#maps">Handling Map Responses</a>
* <a href="#security">Custom Security Interceptors</a>
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
The plugin not includes the ability to configure some defaults like telling all services to default to soap 1.2 or specifying the cxf servlet runtime url.  The default config is in `DefaultCxfConfig.groovy` and may be overridden in your projects Config.groovy by changing any of the node values via standard config closure or dot configuration.

If you wish to override a single value such as soap 1.2 default you would add the following to your Config.groovy:

```
cxf.endpoint.soap12Binding = true
```

Here are the defaults in their entirety:

```groovy
/**
 * Default configuration values for the plugin.  You can override in Config.groovy
 */
cxf {
    servlet {

        /**
         * cxf.servlet.loadOnStartup
         * <p>
         * Specifies the order in which to load the servlet. Lower positive
         * values load first, while negative or unspecified mean that the
         * sevlet can be loaded at anytime.
         */
        loadOnStartup = 10

        /**
         * cxf.servlet.defaultServlet
         * <p>
         * When multiple servlets are defined by the {@code cxf.servlets}
         * configuration value this specifies the default binding for endpoints
         * that don't explicitly define a {@code static servlet = name}. If
         * this value is not set then the first alphabetically will be used.
         */
        //defaultServlet = 'CxfServlet'
    }

    /**
     * cxf.servlets
     * <p>
     * A map of Servlet Name -> Servlet Mapping Pattern. If multiple Cxf
     * servlets are required or a different mapping pattern is needed this
     * configuration allows that.
     */
    servlets = [
            CxfServlet: '/services/*'
    ]

    endpoint {

        /**
         * cxf.endpoint.soap12Binding
         * <p>
         * Sets the Cxf Server Factory to generate a Soap 1.2 binding. This can
         * also be set on a per endpoint basis using
         * {@code static soap12 = true}.
         */
        soap12Binding = false
    }
}
```

<p align="right"><a href="#Top">Top</a></p>
<a name="soap"></a>
EXPOSING CLASSES VIA SOAP
-----------------
There are many ways to configure the plugin.  The legacy way is to use the `static expose = ['cxf']` in your service class.  Legacy support for both `static expose = ['cxf']` and `static expose = ['cxfjax']` services remains, but the new preferred way is to use one of the following methods of exposure:

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

*Please note that while possible to use the string literals behind the `EndpointType` constants, using the constant is much preferred and will cause less issues with spellings and upgrade issues in the future.*

*Please note that that using list annotations is also possible such as `static expose = [EndpointType.SIMPLE]` although there should be no need to ever use more than one cxf expose keyword, other plugins may overlap with this keywork and you can add these other keywords to the list.*

Another way to expose a service or endpoint is to simply annotate it with the `@WebService` annotation.  Please note that when exposing using only the annotation, the plugin wiring will not do any of the magic or recognize anything other than the cxf annotations.  You will be reliant entirely on setting all the appropriate annotations to wire up your service.  This may very well be your intention if you choose to go down this path and is entirely a feasable option.

In the example below an interface is annotated and the service class implements that interface.  I would recommend annotated the interface as it makes for a cleaner implementation of which methods you wish to expose via an interface contract and annotations.

```groovy
@WebService
interface BookStore {
    @WebResult(name='book')
    @WebMethod Book findBookByIsbnNumber(
            @WebParam(name="number") String number
    ) throws InvalidIsbnFormatException

    @WebResult(name='book')
    @WebMethod Book findBookByIsbn(
            @WebParam(name="isbn") Isbn isbn
    ) throws InvalidIsbnFormatException
}
```

```groovy
class BookStoreEndpoint implements BookStore {

    Book findBookByIsbnNumber(final String number) throws InvalidIsbnFormatException {
        Isbn isbn = new Isbn(number: number)
        return validateIsbnAndReturnBook(isbn)
    }

    Book findBookByIsbn(final Isbn isbn) throws InvalidIsbnFormatException {
        return validateIsbnAndReturnBook(isbn)
    }

    //this is not exposed via cxf as it is not annotated or part of the interface
    Book validateIsbnAndReturnBook(final Isbn isbn) {
        isbn.validate()

        return new Book(title: 'The Definitive Book of Awesomeness',
                authors: ['The Definitive Author of Awesomeness', 'Bob'],
                isbn: isbn)
    }
}
```

<p align="right"><a href="#Top">Top</a></p>
<a name="soap12"></a>
USING SOAP 1.2
-----------------
To tell a service to default to SOAP 1.2 instead of 1.1 simply add the following line to your service class:

```
    static soap12 = true
```

If you wish to use SOAP 1.2 as the default in ALL of your services you can add the above line to all exposed classes or simply change the default via Config.groovy

```
cxf.endpoint.soap12Binding = true
```

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
<a name="security"></a>
CUSTOM SECURITY INTERCEPTORS
---------------
Until the next version of the plugin supports better integration for wiring interceptors you can hook up in/out interceptors at boot time in BootStrap.config as follows (mileage may vary).  When a service class or endpoint is wired via cxf a factory class will be created with the name pattern of `[className]Factory`.  Meaning that an endpoint named `FooEndpoint` would wire up a `fooEndpointFactory` class and `FooService` will wire a `fooServiceFactory`.

In the example below we would be wiring up a simple username/password interceptor to a service named SecureService.  The exmple below is for any version of cxf 2.4+.  At this time the plugin is on version 2.6.1.  Previous to version 2.3.9 wiring this was a bit different.  See <http://www.christianoestreich.com/2012/04/grails-cxf-interceptor-injection/> for more details.

```groovy
    def init = { servletContext ->

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
        secureServiceFactory.getInInterceptors().add(new WSS4JInInterceptor(inProps))
    }
```

<p align="right"><a href="#Top">Top</a></p>
<a name="Future"></a>
FUTURE REVISIONS
---------------

* Easier support for intercetors via class level definition with something like `static inIntercetors = [InterceptorOne, InterceptorTwo]` for example

<p align="right"><a href="#Top">Top</a></p>
<a name="License"></a>
LICENSE
---------------

Copyright 2012 Christian Oestreich

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.