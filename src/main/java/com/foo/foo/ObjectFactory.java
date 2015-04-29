
package com.foo.foo;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.foo.foo package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _SimpleMethod1Response_QNAME = new QName("http://demo.client.cxf/", "simpleMethod1Response");
    private final static QName _SimpleMethod1_QNAME = new QName("http://demo.client.cxf/", "simpleMethod1");
    private final static QName _SimpleMethod2_QNAME = new QName("http://demo.client.cxf/", "simpleMethod2");
    private final static QName _SimpleMethod2Response_QNAME = new QName("http://demo.client.cxf/", "simpleMethod2Response");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.foo.foo
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link SimpleMethod1Response }
     * 
     */
    public SimpleMethod1Response createSimpleMethod1Response() {
        return new SimpleMethod1Response();
    }

    /**
     * Create an instance of {@link SimpleMethod1 }
     * 
     */
    public SimpleMethod1 createSimpleMethod1() {
        return new SimpleMethod1();
    }

    /**
     * Create an instance of {@link SimpleMethod2 }
     * 
     */
    public SimpleMethod2 createSimpleMethod2() {
        return new SimpleMethod2();
    }

    /**
     * Create an instance of {@link SimpleMethod2Response }
     * 
     */
    public SimpleMethod2Response createSimpleMethod2Response() {
        return new SimpleMethod2Response();
    }

    /**
     * Create an instance of {@link SimpleRequest }
     * 
     */
    public SimpleRequest createSimpleRequest() {
        return new SimpleRequest();
    }

    /**
     * Create an instance of {@link SimpleResponse }
     * 
     */
    public SimpleResponse createSimpleResponse() {
        return new SimpleResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SimpleMethod1Response }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://demo.client.cxf/", name = "simpleMethod1Response")
    public JAXBElement<SimpleMethod1Response> createSimpleMethod1Response(SimpleMethod1Response value) {
        return new JAXBElement<SimpleMethod1Response>(_SimpleMethod1Response_QNAME, SimpleMethod1Response.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SimpleMethod1 }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://demo.client.cxf/", name = "simpleMethod1")
    public JAXBElement<SimpleMethod1> createSimpleMethod1(SimpleMethod1 value) {
        return new JAXBElement<SimpleMethod1>(_SimpleMethod1_QNAME, SimpleMethod1 .class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SimpleMethod2 }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://demo.client.cxf/", name = "simpleMethod2")
    public JAXBElement<SimpleMethod2> createSimpleMethod2(SimpleMethod2 value) {
        return new JAXBElement<SimpleMethod2>(_SimpleMethod2_QNAME, SimpleMethod2 .class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SimpleMethod2Response }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://demo.client.cxf/", name = "simpleMethod2Response")
    public JAXBElement<SimpleMethod2Response> createSimpleMethod2Response(SimpleMethod2Response value) {
        return new JAXBElement<SimpleMethod2Response>(_SimpleMethod2Response_QNAME, SimpleMethod2Response.class, null, value);
    }

}
