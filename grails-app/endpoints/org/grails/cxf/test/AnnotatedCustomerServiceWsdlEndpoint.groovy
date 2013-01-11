package org.grails.cxf.test

import org.grails.cxf.test.soap.Customer
import org.grails.cxf.utils.EndpointType
import org.grails.cxf.utils.GrailsCxfEndpoint

import javax.jws.WebService

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
