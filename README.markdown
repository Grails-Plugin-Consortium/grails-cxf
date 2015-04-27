[![Build Status](https://travis-ci.org/Grails-Plugin-Consortium/grails-cxf.png?branch=master)](https://travis-ci.org/Grails-Plugin-Consortium/grails-cxf)

<a name="Top"></a>

*Please note that the current 2.1.x versions use cxf 3.0.4 and WSS4J 2.x is required for security.*

*Please note that in cxf 3.0.4 (plugin v 2.1+) stuff was moved around, A LOT.  For example we had to add org.apache.cxf.xjc.runtime.DataTypeAdapter in place of a previously included item.*

*Please note that the current 2.x versions will only with with grails 2.4+.  If you wish to use this with 2.0 - 2.3.x please use version 1.1.4 of the plugin.*

GRAILS CXF PLUGIN
======

* <a href="#Introduction">Introduction</a>
* <a href="#Script">Wsdl2java Script</a>
* <a href="#plugin">Plugin Configuration</a>
* <a href="#soapannotation">Exposing Classes via Annotation</a>
* <a href="#soap">Exposing Classes via Properties</a>
* <a href="#soap12">Using SOAP 1.2</a>
* <a href="#wsdl">Wsdl First Services</a>
* <a href="#jax">JAX Web Service Gotcha's</a>
* <a href="#lists">Handling List Responses</a>
* <a href="#hasMany">Handling hasMany Mappings</a>
* <a href="#maps">Handling Map Responses</a>
* <a href="#security">Custom Security Interceptors</a>
* <a href="#console">Enabling Logging of SOAP Messages</a>
* <a href="#Demo">Demo Project</a>
* <a href="#Issues">Issues</a>
* <a href="#Build">Build</a>
* <a href="#Change">Change Log</a>
* <a href="#Future">Future Revisions</a>
* <a href="#License">License</a>

<a name="Introduction"></a>
INTRODUCTION
---------------

The Grails Cxf plugin makes exposing classes (services and endpoints) as SOAP web services easy and painless.  Since version 1.0.0, it has been rewritten and enhanced to support more features including the migration to grails 2.x.

The current cxf version is [2.6.2](https://issues.apache.org/jira/secure/ReleaseNote.jspa?projectId=12310511&styleName=Html&Create=Create&version=12321668)

**New** in 1.1.0 is the ability to define custom names and addressing for the services as well as the ability to wire via a single annotation instead of multiple static properties.  In addition, the ability to define custom interceptor beans by name is now available in the annotation.

Some new things as of version 1.0.0 are as follows:

* The plugin will autowire configured classes in the grails-app\endpoints\** AND the grails-app\services\** directories
* Endpoint creation scripts create-endpoint and create-endpoint-simple will create cxf artefacts in grails-app\endpoints
* Service creation scripts create-cxf-service and create-cxf-service-simple will create cxf artefacts in grails-app\services
* The suggested pattern to isolate cxf endpoints is to have endpoints live in grails-app/endpoints directory (or you can use grails-app/services for overlapping and shared services)
* Built in support for simple Map response type handling via `@XmlJavaTypeAdapter(GrailsCxfMapAdapter.class)` method annotation has been included to use or to kick start your own map adapter creation
* Many new examples to help with configuration can be found in the source via functional specs and test classes at <https://github.com/Grails-Plugin-Consortium/grails-cxf>
* Default plugin configuration is provided via `DefaultCxfConfig.groovy`.  Although usually not necessary, you can override in your project's Config.groovy
* The default url for wsdl viewing remains `http://.../[app name if not root]/services` as it was in previous versions.  Multiple cxf servlet endpoints can be configured or the default changed via Config.goovy
* Wsdl First services are now available to use <a href="#wsdl">example below</a>

<p align="right"><a href="#Top">Top</a></p>
<a name="Script"></a>
WSDL2JAVA SCRIPT
---------------
Included with the plugin is a convenient script to generate java code from a wsdl.  Please note that the grails cxf-client plugin also includes a similar script (wsdl2java) albeit using different configuration to create java files from Config.groovy instead of command line params.

*Note:* You may need to quote the options due to how grails and groovy interact with command line parameters such as `grails wsdl-to-java "--wsdl=path --mark"`

```
usage: grails wsdl-to-java --wsdl=<path to wsdl>
    [--package=<package>] [--fe=frontend-name] [--db=databinding-name]
    [--wv=wsdl-version] [--sn=service-name] [--b=binding-name]
    [--catalog=catalog-file-name] [--d output-directory] [--compile]
    [--classdir=compile-class-dir] [--client] [--server]
    [--impl] [--all] [--ant] [--autoNameResolution] [--exsh=(true/false)]
    [--dns=(true/false)] [--dex=(true/false)] [--validate] [--keep] [--noAddressBinding]
    [--exceptionSuper] [--reserveClass=classname] [--allowElementReferences<=true>]
    [--asyncMethods=foo,bar,...] [--bareMethods=foo,bar,...]
    [--mimeMethods=foo,bar,...] [--mark]

Script Options:
  -help, --help           Prints this help message
```

I tried to map as close as possible the options from the wsdl2java script.

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
<a name="soapannotation"></a>
EXPOSING CLASSES VIA ANNOTATION
-----------------
Added in version 1.1.0 was the ability to expose classes via the new `@GrailsCxfEndpoint()` annotation.  When using the annotation, the property values will only be used if the corresponding annotation value is not provided or is set to the default value.  The following are available to configure via the annotation:

```groovy
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface GrailsCxfEndpoint {
    String address() default ""
    EndpointType expose() default EndpointType.JAX_WS
    boolean soap12() default false
    String wsdl() default ""
    String[] excludes() default []
    String[] inInterceptors() default []
    String[] outInterceptors() default []
    String[] inFaultInterceptors() default []
    String[] outFaultInterceptors() default []
}
```

**ADDRESS**

The address property is used to adjust the endpoint address that the service will be deployed to.  By default if not provided or is the value is empty (""), this will be the name of the Service or Endpoint with the first letter lowercase and the word Endpoint or Service removed from the end of the name.  The default behavior would deploy the `BoatService` as `/services/boat` and the VeryGoodEndpoint as `/services/veryGood`.

If you wish to override this and provide your own service name or address (for versioning support for example) you may set this value.

```groovy
@GrailsCxfEndpoint(address='/v2/custom/path')
class CarService {
    ...
}
```

The above would be deployed to `/services/v2/custom/path`.

If you wish to simply version the service you could use the special `#name` keyword in the address or manually set it to the name that matches the rule above.

```groovy
@GrailsCxfEndpoint(address='/v2/#name') //or address='/v2/car'
class CarService {
    ...
}
```

This would be deployed as `/services/v2/car`

**EXPOSE**

The `expose` property will tell the plugin how you wish to expose.  The default is `EndpointType.JAX_WS` which is the same as the following:

```groovy
@GrailsCxfEndpoint(expose=EndpointType.JAX_WS)
class CarService {
    ...
}
```

To expose as a simple endpoint <http://cxf.apache.org/docs/simple-frontend-configuration.html>:

```groovy
    expose = EndpointType.SIMPLE
```

To expose as a jax web service endpoint <http://cxf.apache.org/docs/jax-ws-configuration.html>:

*Please note that using the JAX_WS type requires you to annotate your methods with `@WebMethod`, `@WebResult` and `@WebParam`.*

```groovy
    expose = EndpointType.JAX_WS
```

To expose as a wsdl first jax web service endpoint <http://cxf.apache.org/docs/jax-ws-configuration.html>:

```groovy
     expose = EndpointType.JAX_WS_WSDL
     wsdl = 'org/grails/cxf/test/soap/CustomerService.wsdl' //your path (preferred) or url to wsdl
```

To expose as a jax rest service endpoint <http://cxf.apache.org/docs/jax-rs.html>:

```groovy
     expose = EndpointType.JAX_RS
```

**SOAP12**

To tell a service to default to SOAP 1.2 instead of 1.1 simply set this to `true`. The default value is `false` which will use SOAP 1.1.

```groovy
@GrailsCxfEndpoint(soap12=true)
class CarService {
    ...
}
```

**EXCLUDES**

If you wish to exclude methods from exposure when using `EndpointType.SIMPLE` you may provide an array of method name strings to the excludes param.  The groovy methods are ignored by default and no action is necessary to remove the groovy/meta stuff.

```groovy
@GrailsCxfEndpoint(expose=EndpointType.SIMPLE, excludes=['methodOne', 'methodTwo'])
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

Example is available at [AnnotatedCustomerServiceWsdlEndpoint.groovy](https://github.com/Grails-Plugin-Consortium/grails-cxf/blob/master/grails-app/endpoints/org/grails/cxf/test/AnnotatedCustomerServiceWsdlEndpoint.groovy).

<a name="interceptors"></a>
**ININTERCEPTORS**

This is a list of bean names in `List<String>` to inject to the cxf service endpoint.  You will need to define your interceptor beans via normal spring dsl (in resources.groovy for example).

This is helpful when the default cxf annotation of `@org.apache.cxf.interceptor.InInterceptors (interceptors = {"com.example.Test1Interceptor" })` does not satisfy your needs.

When chosing between the this property and the cxf provided one, if you require value injection, the cxf provided annotation will most likely **NOT** meet your needs and you should use this property instead.

*Note: Make sure to set any beans you wish injected into your interceptors to `bean.autowire = 'byName'` or use the `@Autowire` annotation.*

[CustomLoggingInInterceptor.groovy](https://github.com/Grails-Plugin-Consortium/grails-cxf/blob/master/src/groovy/org/grails/cxf/test/soap/security/CustomLoggingInInterceptor.groovy)
```groovy
@NoJSR250Annotations
public class CustomLoggingInInterceptor extends AbstractLoggingInterceptor {

    private static final Logger LOG = LogUtils.getLogger(CustomLoggingInInterceptor)
    def name
//    @Autowired //or set bean spring dsl to bean.autowire = "byName"
    InjectedBean injectedBean

    public CustomLoggingInInterceptor() {
        super(Phase.RECEIVE);
        log LOG, "Creating the custom interceptor bean"
    }

    public void handleMessage(Message message) throws Fault {
        //get another web service bean here by name and call it

        //Check to see if cxf annotations will inject the bean (looks like no!)
        log LOG, injectedBean?.name ?: "FAIL - NOT SET"
        log LOG, "$name :: I AM IN CUSTOM IN LOGGER!!!!!!!"
    }

    @Override
    protected Logger getLogger() {
        LOG
    }
}
```

[resources.groovy](https://github.com/Grails-Plugin-Consortium/grails-cxf/blob/master/grails-app/conf/spring/resources.groovy)
```groovy
import org.grails.cxf.test.soap.security.CustomLoggingInInterceptor
import org.grails.cxf.test.soap.security.InjectedBean

beans = {
    customLoggingInInterceptor(CustomLoggingInInterceptor) {
        name = "customLoggingInInterceptor"
    }

    injectedBean(InjectedBean) { bean ->
        bean.autowire = 'byName'
        name = "i was injected"
    }
}
```

[AnnotatedInterceptorService.groovy](https://github.com/Grails-Plugin-Consortium/grails-cxf/blob/master/grails-app/services/org/grails/cxf/test/AnnotatedInterceptorService.groovy)
```groovy
@GrailsCxfEndpoint(inInterceptors = ["customLoggingInInterceptor"])
class AnnotatedInterceptorService {

    @WebMethod(operationName="simpleMethod")
    @WebResult(name="simpleResult")
    String simpleMethod(@WebParam(name="param") String param) {
        return param.toString()
    }
}
```

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

<p align="right"><a href="#Top">Top</a></p>
<a name="soap"></a>
EXPOSING CLASSES VIA PROPERTIES
-----------------
*v1.1.0 Note: The usage of the static properties is considered deprecated and switching to the new <a href="#soapannotation">annotation</a> is a cleaner implementation.  Also to note that custom interceptors are only supported in the annotation.*

There are many ways to configure the plugin using static properties.  The legacy way was to use the `static expose = ['cxf']` in your service classes.  Legacy support for both `static expose = ['cxf']` , `static expose = ['cxfjax']` and `static expose = ['cxfrs']` services remains, but the new preferred way is to use one of the following methods of exposure.

The properties available for use are:

```
static expose = ...
static soap12 = ...
static address = ...
static wsdl = ...
static excludes = ...
```

See the description above in the <a href="#soapannotation">Exposing Classes via Annotation</a> section for more details on the meanings the of properties.  The end result of the static properties is the same as the equivalent annotation properties.

*Note: While you can mix the `@GrailsCxfEndpoint` annotation and the static properties, anything defined in the annotation will override any static property value that you set.*

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

*Please note that that using list annotations is also possible such as `static expose = [EndpointType.SIMPLE]` although there should be no need to ever use more than one cxf expose keyword, other plugins may overlap with this keyword and you can add these other keywords to the list.*

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
*v1.1.0+ Note: Make sure to read <a href="#soapannotation">Exposing Classes via Annotation</a>*

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
*v1.1.0+ Note: Make sure to read <a href="#soapannotation">Exposing Classes via Annotation</a>*

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

    def notExposed(){
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
    @WebResult(name='entries')
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
<a name="console"></a>

ENABLING LOGGING OF SOAP MESSAGES
---------------
If you would like to view the raw soap in the console/log files add the follow:

JVM startup params:
```
-Dorg.apache.cxf.Logger=org.apache.cxf.common.logging.Log4jLogger
```

Logging config:
```groovy
log4j {
    ...
  info 'org.apache.cxf' //debug, etc
}
```

<p align="right"><a href="#Top">Top</a></p>
<a name="security"></a>

CUSTOM SECURITY INTERCEPTORS
---------------
Using security on your webservice may be as simple as providing an in or out interceptor, but in some cases like when using WSS4J, you may be required to do a bit more work.

Since WSS4J configuration requires more than just creating and using an interceptor bean and requires you to provide some configuration details, the easiest way to set it up is to inject the interceptor in your `BootStrap.groovy`.

To use WSS4J on the class `AnnotatedSecureService` we would need to inject the object onto the `annotatedSecureServiceFactory` object in the bootstrap.  All Service and Endpoint Classes will create a factory class that is used for cxf that will simply be named by appending `Factory` to the end with a lowercase first letter.

##  WSS4J 1.x ##

BootStrap.groovy
```groovy
class BootStrap {

    ServerFactoryBean annotatedSecureServiceFactory

    def init = { servletContext ->
        //Register some wss4j security
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
        //These can be added here or taken care of in the @GrailsCxfEndpoint annotation
        //annotatedSecureServiceFactory.getProperties(true).put("ws-security.enable.nonce.cache","false")
        //annotatedSecureServiceFactory.getProperties(true).put("ws-security.enable.timestamp.cache","false")
    }
}
```

##  WSS4J 2.x ##

Bootstrap.groovy
``` groovy
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
```

## WSS4J Generic ##

To get the webservice working in the current version of CXF we must disable the playback cache by setting the properties `ws-security.enable.nonce.cache=false` and `ws-security.enable.timestamp.cache=false`.  This is done by using the `@GrailsCxfEndpointProperty`.

[AnnotatedSecureService.groovy](https://github.com/Grails-Plugin-Consortium/grails-cxf/blob/master/grails-app/services/org/grails/cxf/test/AnnotatedSecureService.groovy)
```groovy
package org.grails.cxf.test

import org.grails.cxf.utils.EndpointType
import org.grails.cxf.utils.GrailsCxfEndpoint
import org.grails.cxf.utils.GrailsCxfEndpointProperty

import javax.jws.WebMethod
import javax.jws.WebParam
import javax.jws.WebResult

@GrailsCxfEndpoint(expose = EndpointType.JAX_WS,properties = [@GrailsCxfEndpointProperty(name = "ws-security.enable.nonce.cache", value = "false"), @GrailsCxfEndpointProperty(name = "ws-security.enable.timestamp.cache", value = "false")])
class AnnotatedSecureService {

    @WebMethod(operationName = "simpleMethod")
    @WebResult(name = "simpleResult")
    String simpleMethod(@WebParam(name = "param") String param) {
        return param.toString()
    }
}
```

Anyone invoking your server will be required to set the security header to simple user name and password.  You can use a similar mechanism for certificate security.  Configuring WSS4J using certs examples are available around the web.

##SOAP##

The soap message will look like the following:

```xml
<soap-env:Envelope xmlns:soap-env="http://schemas.xmlsoap.org/soap/envelope/" xmlns:test="http://test.cxf.grails.org/" xmlns:soapenv="soapenv">
   <soap-env:Header>
      <wsse:Security soapenv:mustUnderstand="1" xmlns:wsse="http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd" xmlns:wsu="http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd">
         <wsse:UsernameToken wsu:Id="UsernameToken-13">
            <wsse:Username>wsuer</wsse:Username>
            <wsse:Password Type="http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordText">secret</wsse:Password>
            <wsse:Nonce EncodingType="http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-soap-message-security-1.0#Base64Binary">y98XJq+CCJh2JfFu4jqjRQ==</wsse:Nonce>
            <wsu:Created>2013-01-18T16:19:17.950Z</wsu:Created>
         </wsse:UsernameToken>
      </wsse:Security>
   </soap-env:Header>
   <soap-env:Body>
      <test:simpleMethod>
         <param>hello</param>
      </test:simpleMethod>
   </soap-env:Body>
</soap-env:Envelope>
```

##WSLITE##

Adding the username and plain text credentials to [wslite](https://github.com/jwagenleitner/groovy-wslite) looks like this:

```groovy
SOAPResponse response = client.send {
    envelopeAttributes "xmlns:test": 'http://test.cxf.grails.org/', "xmlns:soapenv":"soapenv"
    version SOAPVersion.V1_1
    header {
        'wsse:Security'('soapenv:mustUnderstand': "1", 'xmlns:wsse': 'http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd', 'xmlns:wsu': 'http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd') {
            'wsse:UsernameToken'('wsu:Id':"UsernameToken-13") {
                'wsse:Username'(username)
                'wsse:Password'('Type':'http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordText',password)
                'wsse:Nonce'('EncodingType':'http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-soap-message-security-1.0#Base64Binary',new String(password.bytes.encodeBase64().toString()))
                'wsu:Created'('2013-01-18T16:19:17.950Z')
            }
        }
    }
    body {
        'test:simpleMethod' {
            param(legacyParam)
        }
    }
}
```

##CXF CLIENT PLUGIN##

Configuring this in the [cxf-client](https://github.com/Grails-Plugin-Consortium/grails-cxf-client) grails plugin would look similar to the following:

```groovy
cxf {
    client {
         secureServiceClient {
            ...
            secured = true
            username = "wsuser"
            password = "secret" //or "password"
            ...
        }
    }
}
```

There are additional examples available using the [cxf-client-demo](https://github.com/Grails-Plugin-Consortium/grails-cxf-client-demo) project.

##OTHER##

See the <a href="#interceptors">annotations documentation</a> above or use the [cxf provided interceptor annotations](http://cxf.apache.org/docs/interceptors.html) for use of annotations in general.

<p align="right"><a href="#Top">Top</a></p>
<a name="Demo"></a>
DEMO PROJECT
---------------
The best way to play around with wiring up services/endpoints is to clone this project and then `grails run-app` or `grails test-app` on it.  There are a lot of sample endpoints and services available in the source that are not included inthe plugin bundle.

<p align="right"><a href="#Top">Top</a></p>
<a name="Issues"></a>
ISSUES
---------------
I will be using the github issue tracker <https://github.com/Grails-Plugin-Consortium/grails-cxf/issues> for issues and questions.


<p align="right"><a href="#Top">Top</a></p>
<a name="Build"></a>
BUILD SERVER
-----------------

[![Build Status](https://travis-ci.org/Grails-Plugin-Consortium/grails-cxf.png?branch=master)](https://travis-ci.org/Grails-Plugin-Consortium/grails-cxf)

<p align="right"><a href="#Top">Top</a></p>
<a name="Change"></a>
CHANGE LOG
---------------
* v 2.1.1
    * Upgrading to cxf 3.0.4, wss4j 2.0.3 and sun jaxb 2.2.11 and spring 4.0.9-RELEASE
    * Fixing wsdl2java script for bareMethods.  Added new -bareMethodsAll that takes no params for doing all methods as bare (no out params).  The -bareMethods requires one or more params.
    * Still working on grails 3 migration

* v 2.0.2
    * Upgrading CXF to 2.6.16
    * Upgrading Spring deps to 4.0.8.RELEASE
    
    
* v 2.0.1
    * Removing artifacts that crept into the plugin

* v 2.0
    * Update grails version
    * Removed spock plugin (now bundled with grails)
    * Added needed spring dependencies because of upgrade to Spring 4
    * Refresh GenericApplicationContext _before_ use (otherwise Spring 4 blows up)
    * Removed use of ConfigurationHolder
    * Fixed applicationContext.xml for Grails 2.4
    * Upgrade CXF to 2.6.6. Newer versions breaks testsuite
    * 
* v1.1.4
    * Fixing the wsdl2java script to be able to be used externally as the default is no longer just main in the script.
    * This is the last version compatible with grails < 2.4.  If you are on any version bewteen 2.0 and 2.3.x please use this version.
    
* v1.1.0
    * Adding support to use annotation driven service configuration via `@GrailsCxfEndpoint(...)` to deprecate the usage of the current several static properties on a class
    * Adding support for versioning through use of the `address` property on the annotation (**deprecated**: and via a static property `static address = '/v2/#name'`) *(#name is special and will use the default service name)*
    * Adding support to override the default service name (via address) by NOT using the `#name` special property in the address via `address = '/path/v2/customName'`
    * Adding support for injecting interceptors to services via the new `@GrailsCxfEndpoint(...)` annotation.  Cxf also provides similar functionality via [annotations](http://cxf.apache.org/docs/interceptors.html).  The difference being apache cxf **does not** allow bean or value injection to the provided classes as it appears to not understand `@Autowire` internally or deal with spring injection.
    * Added a crap-ton (which is a lot) of [new specs](https://github.com/Grails-Plugin-Consortium/grails-cxf/tree/master/test/functional/org/grails/cxf/test) to test these scenarios and annotation

* v1.0.8
    * No logical code changes, code cleanup and removal of unused items - thanks @burtbeckwith

* v1.0.7
    * Use jaxb 2.2.6 due to bug fix for exception marshaling <http://java.net/jira/browse/JAXB-814>
    * Added functional tests for ensuring methods that return exceptions object (not thrown) don't fail in marshaling.

* v1.0.6
    * Config slurper was looking for base grails.cxf instead of cxf.  Fixed to use cxf as root for overrides.

* v1.0.5
    * Removing @Commons to retain compatibility with grails 1.3.x+

* v1.0.1 - 1.0.4
    * Several successive releases to address issues raised after deployment

* v1.0.0 - Initial re-release of the grails cxf plugin with ground up rewrite.  Thanks goes out to Ben Doerr <https://github.com/bendoerr> who was pivital in getting this project off the ground again.

<p align="right"><a href="#Top">Top</a></p>
<a name="Future"></a>
FUTURE REVISIONS
---------------

<del>* Easier support for intercetors via class level definition with something like `static inIntercetors = [InterceptorOne, InterceptorTwo]` for example</del>

<p align="right"><a href="#Top">Top</a></p>
<a name="License"></a>
LICENSE
---------------

Copyright 2013 Christian Oestreich

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
