<a name="Top"></a>

GRAILS CXF PLUGIN
======

* <a href="#Introduction">Introduction</a>
* <a href="#Script">Wsdl2java Script</a>
* <a href="#plugin">Plugin Configuration</a>
* <a href="#soap">Exposing Classes via SOAP</a>
* <a href="#soap12">Using SOAP 1.2</a>
* <a href="#wsdl">Wsdl First Services</a>
* <a href="#jax">JAX Web Service Gotcha's</a>
* <a href="#lists">Handling List Responses</a>
* <a href="#hasMany">Handling hasMany Mappings</a>
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

The Grails Cxf plugin makes exposing classes (services and endpoints) as SOAP web services easy and painless.  Since version 1.0.0, it has been rewritten and enhanced to support more features including the migration to grails 2.x.

The current cxf version is [2.6.2](https://issues.apache.org/jira/secure/ReleaseNote.jspa?projectId=12310511&styleName=Html&Create=Create&version=12321668)

Some new things as of version 1.0.0 are as follows:

* The plugin will autowire configured classes in the grails-app\endpoints\** AND the grails-app\services\** directories
* Endpoint creation scripts create-endpoint and create-endpoint-simple will create cxf artefacts in grails-app\endpoints
* Service creation scripts create-cxf-service and create-cxf-service-simple will create cxf artefacts in grails-app\services
* The suggested pattern to isolate cxf endpoints is to have endpoints live in grails-app/endpoints directory (or you can use grails-app/services for overlapping and shared services)
* Built in support for simple Map response type handling via `@XmlJavaTypeAdapter(GrailsCxfMapAdapter.class)` method annotation has been included to use or to kick start your own map adapter creation
* Many new examples to help with configuration can be found in the source via functional specs and test classes at <https://github.com/thorstadt/grails-cxf>
* Default plugin configuration is provided via `DefaultCxfConfig.groovy`.  Although usually not necessary, you can override in your project's Config.groovy
* The default url for wsdl viewing remains `http://.../[app name if not root]/services` as it was in previous versions.  Multiple cxf servlet endpoints can be configured or the default changed via Config.goovy
* Wsdl First services are now available to use <a href="#wsdl">example below</a>

<p align="right"><a href="#Top">Top</a></p>
<a name="Script"></a>
WSDL2JAVA SCRIPT
---------------
Included with the plugin is a convenient script to generate java code from a wsdl.  Please note that the grails cxf-client plugin also includes a similar script (wsdl2java) albeit using different configuration to create java files from Config.groovy instead of command line params.

```
usage: grails wsdl-to-java --wsdl=<path to wsdl> [--package=<package>]

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
The plugin not includes the ability to configure some defaults such as defaulting all services to soap 1.2 or specifying the cxf servlet runtime url.  The default config is in `DefaultCxfConfig.groovy`.  The properties contained within may be overridden in your project's Config.groovy by changing any of the node values via standard config closure or dot configuration.

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
There are many ways to configure the plugin.  The legacy way was to use the `static expose = ['cxf']` in your service classes.  Legacy support for both `static expose = ['cxf']` , `static expose = ['cxfjax']` and `static expose = [''cxfrs'']` services remains, but the new preferred way is to use one of the following methods of exposure:

**SIMPLE SERVICES**

To expose as a simple endpoint <http://cxf.apache.org/docs/simple-frontend-configuration.html> add the following to your endpoint or service class:

```groovy
    static expose = EndpointType.SIMPLE
```

It states clearly in the documentation at <http://cxf.apache.org/docs/simple-frontend.html> the following:

*There is a known issue for the JAXB data binding with POJO, please see Dan Kulp's comment in <https://issues.apache.org/jira/browse/CXF-897>.  If you want to use JAXB binding you should use the JAX-WS Frontend. Mixing Simple Frontend and JAXB binding leads to problems. The article A simple JAX-WS service shows a code first JAX-WS service that is almost as easy to use as the Simple Frontend.*

**JAX WEB SERVICES**

To expose as a jax web service endpoint <http://cxf.apache.org/docs/jax-ws-configuration.html> add the following to your endpoint or service class:

```groovy
    static expose = EndpointType.JAX_WS
```

**JAX WEB WSDL SERVICES**

To expose as a wsdl first jax web service endpoint <http://cxf.apache.org/docs/jax-ws-configuration.html> add the following to your endpoint or service class:

```groovy
     static expose = EndpointType.JAX_WS_WSDL
     static wsdl = 'org/grails/cxf/test/soap/CustomerService.wsdl' //your path (preferred) or url to wsdl
```

**JAX REST SERVICES**

To expose as a jax rest service endpoint <http://cxf.apache.org/docs/jax-rs.html> add the following to your endpoint or service class:

```groovy
     static expose = EndpointType.JAX_RS
```

*Please note that while possible to use the string literals behind the `EndpointType` constants, using the constant is much preferred and will cause less issues with spellings and upgrade issues in the future.*

*Please note that that using list annotations is also possible such as `static expose = [EndpointType.SIMPLE]` although there should be no need to ever use more than one cxf expose keyword, other plugins may overlap with this keywork and you can add these other keywords to the list.*

**Using Apache CXF Without Plugin Wiring**

Since all the appropriate apache cxf libs are included with this plugin, another way to expose a service or endpoint is to simply annotate it with the `@WebService` annotation.  Please note that when exposing using only the annotation, the plugin wiring will not do any of the magic or recognize anything other than the cxf annotations.  You will be reliant entirely on setting all the appropriate annotations to wire up your service.  This may very well be your intention if you choose to go down this path and is entirely a feasable option.

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

If you wish to use SOAP 1.2 as the default in ALL of your services/endpoints you can add the above line to all exposed classes or simply change the default via Config.groovy

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
<a name="jax"></a>
JAX WEB SERVICE GOTCHA'S
-----------------

**Gotcha**

Do not try and annotate methods with default params as CXF chokes on these due to groovy actually adding multiple methods under the cover with the same name and annotations which causes CXF to puke when it tried to add these duplicated named web service method to the binding.

This will **not** work!

```groovy

static expose = EndpointType.JAX_WS

@WebResult(name = 'data')
@WebMethod(operationName = 'getData')
List<Medication> getData(@WebParam(name = 'id') String id, @WebParam(name = 'type') String type, @WebParam(name = 'isGeneric') Boolean isGeneric = true) {
    ...
}
```

You should instead create a new method(s) with params defined that you need and pass through to the base service.

```groovy

static expose = EndpointType.JAX_WS

@WebResult(name = 'data')
@WebMethod(operationName = 'getDataWithGeneric')
List<Medication> getDataWithGeneric(@WebParam(name = 'id') String id, @WebParam(name = 'type') String type, @WebParam(name = 'isGeneric') Boolean isGeneric) {
    this.getData(id, type, isGeneric)
}

@WebResult(name = 'data')
@WebMethod(operationName = 'getDataNoGeneric')
List<Medication> getDataNoGeneric(@WebParam(name = 'id') String id, @WebParam(name = 'type') String type) {
    this.getData(id, type)
}

List<Medication> getData(String id, String type, Boolean isGeneric = true) {
    ...
}
```

Or if you want to leave your default params on your service, make an interface for your service and annotated that.  Then simply implement the interface on your service.  The wiring will be done against the interface for these methods.  **All methods to be exposed (not just the ones with defaults) must then be defined and annotated in the interface**.  You must include the `@WebService` annotation on the service and the `expose` on the service class.

```groovy
//Interface annotation is REQUIRED!
@WebService
public interface DataServiceContract {
    @WebMethod(operationName="getTypes")
    @WebResult(name="types")
    @XmlJavaTypeAdapter(GrailsCxfMapAdapter.class)
    Map<String, Integer> getTypes()

    @WebResult(name = 'datas')
    @WebMethod(operationName = 'getData')
    List<Data> getData(@WebParam(name='type') String type, @WebParam(name='loadChildren') Boolean traverse, @WebParam(name='loadMedications') Boolean medications)
}

//Service class
class DataService implements DataServiceContract {

    //this is REQUIRED!
    static expose = EndpointType.JAX_WS

    @Cacheable("ClassificationServiceCache")
    Map<String, Integer> getTypes() {
       //do work
    }

    //can use default params, but not from cxf endpoint...
    //funny things might happen if you invoke service will nulls and expect some value like true|false
    List<Data> getData(String type, Boolean traverse = true, Boolean isGoodData = false){
        //do work
    }
}
```

--------------------

**Gotcha**

Currently there seems to be an issue dealing with `List` response types and services exposed via 'cxfjax' and EndpointType.JAX_WS.  Wiring will fail if you try and use `@XmlAccessorType(XmlAccessType.FIELD)`.  You must use `@XmlAccessorType(XmlAccessType.NONE)` and annotate your fields explicitly that you want exposed.  I am looking into this issue, but currently I know of no other way to make Lists work.

You will have to add the properties you want exposed that would normally be auto exposed via the FIELD type such as `Long id`, `Long version`, etc.  If you specify the service method return type as `def` you will probably see JAXB complain about not knowing about ArrayLists.

Service class:
```groovy
@WebResult(name = 'page')
@WebMethod(operationName = 'getMeSomePages')
List<Page> getMeSomePages() {
    ...
}
```

Domain Object:
```groovy
@XmlAccessorType(XmlAccessType.NONE)
class Page implements Serializable {
    @XmlElement
    Long id
    @XmlElement
    String name
    @XmlElement
    Integer number
    @XmlElement
    Long version

    static hasMany = [words: Word]

    @XmlElementWrapper(name="words")
    @XmlElement(name="word")
    List<Word> words

    static mapping = {
        version true
    }
}
```

<p align="right"><a href="#Top">Top</a></p>
<a name="lists"></a>
HANDLING LIST RESPONSES
-----------------
As with the previous version of the plugin, it is necessary to annotate your domain classes with the following annotation to allow JAXB to correctly serialize/deserialize your domain ojbects.

```java
@XmlAccessorType(XmlAccessType.FIELD)
```

Example:
```groovy
@XmlAccessorType(XmlAccessType.FIELD)
class Book {
    String title
    List<String> authors
    Isbn isbn
}
```


<p align="right"><a href="#Top">Top</a></p>
<a name="hasMany"></a>
HANDLING HASMANY MAPPINGS
-----------------
If your domain object contains a `static hasMany = [foos:Foo]` and you would like to have these foos included in a service response you will need to do a bit of extra work in the domain object containing the hasMany clause.

To get the hasMany mappings to show up you will need to add the typed (List, Set, SortedSet, etc) variable to your class along with the hasMany statement like below.  See <http://grails.org/doc/latest/guide/GORM.html#5.2.4%20Sets,%20Lists%20and%20Maps> for more details.

```groovy
    static hasMany = [words: Word]

    @XmlElement(name="words")
    List<Word> words
```

*Note If you are using a SortedSet, your object must implement the `Comparable` interface!*

You may also choose to control the way your xml schema looks for these hasMany objects.  You can specify a nested list schema using an `XmlElementWrapper` which would map the xml similar to the following:

```groovy
@XmlAccessorType(XmlAccessType.NONE)
class Page implements Serializable {
    @XmlElement
    String name
    @XmlElement
    Integer number

    static hasMany = [words: Word]

    @XmlElementWrapper(name="words")
    @XmlElement(name="word")
    List<Word> words

    static mapping = {
        version true
    }
}

@XmlAccessorType(XmlAccessType.NONE)
class Word implements Serializable {
    @XmlElement
    String text

}
```

```xml
<page>
    <name>test1</name>
    <number>2</number>
    <words>
        <word><text>i</text></word>
        <word><text>am</text></word>
        <word><text>the</text></word>
        <word><text>doctor</text></word>
    </words>
 </page>
```

If you do not want a wrapper element and prefer to deal with these lists as flattened multiple elements you could use the following code:

```groovy
@XmlAccessorType(XmlAccessType.NONE)
class Page implements Serializable {
    @XmlElement
    String name
    @XmlElement
    Integer number

    static hasMany = [words: Word]

    @XmlElement(name="words")
    List<Word> words

    static mapping = {
        version true
    }
}

@XmlAccessorType(XmlAccessType.NONE)
class Word implements Serializable {
    @XmlElement
    String text
}
```

```xml
<page>
    <name>test1</name>
    <number>2</number>
    <words><text>i</text></words>
    <words><text>am</text></words>
    <words><text>the</text></words>
    <words><text>doctor</text></words>
 </page>
```

Remember that you can use either `@XmlAttribute` for an xml attribute like `<words text="blah" />` or like above with `@XmlElement` where each property annotated as such is a new element node.


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

In the example below we would be wiring up a simple username/password interceptor to a service named SecureService.  The exmple below is for any version of cxf 2.4+.  At this time the plugin is on version 2.6.x.  Previous to version 2.3.9 wiring this was a bit different.  See <http://www.christianoestreich.com/2012/04/grails-cxf-interceptor-injection/> for more details.

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
<a name="Demo"></a>
DEMO PROJECT
---------------
The best way to play around with wiring up services/endpoints is to clone this project and then `grails run-app` or `grails test-app` on it.  There are a lot of sample endpoints and services available in the source that are not included inthe plugin bundle.

<p align="right"><a href="#Top">Top</a></p>
<a name="Issues"></a>
ISSUES
---------------
I will be using the github issue tracker <https://github.com/thorstadt/grails-cxf/issues> for issues and questions.

<p align="right"><a href="#Top">Top</a></p>
<a name="Change"></a>
CHANGE LOG
---------------
* v1.0.5
    * Removing @Commons to retain compatibility with grails 1.3.x+

* v1.0.1 - 1.0.4
    * Several successive releases to address issues raised after deployment

* v1.0.0 - Initial re-release of the grails cxf plugin with ground up rewrite.  Thanks goes out to Ben Doerr <https://github.com/bendoerr> who was pivital in getting this project off the ground again.

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