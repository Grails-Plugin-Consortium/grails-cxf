package org.grails.cxf.test

import org.grails.cxf.test.soap.Customer
import org.grails.cxf.test.soap.CustomerService

class CustomerServiceWsdlEndpoint {
    static wsdl = "org/grails/cxf/test/soap/CustomerService.wsdl"

    CustomerServiceEndpoint customerServiceEndpoint

    List<Customer> getCustomersByName(final String name) {
        customerServiceEndpoint.getCustomersByName(name)
    }

}
