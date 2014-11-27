package showcase.boot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import showcase.boot.domain.Contact;
import showcase.boot.domain.ContactType;
import showcase.boot.domain.Customer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

@RestController
class CustomRest {

    @Autowired
    private CustomerRepository customerRepository;

    @RequestMapping("/customers/generateData")
    int generateData() {
        customerRepository.deleteAllInBatch();
        final int n = 1000;
        Collection<Customer> customers = new ArrayList<Customer>(n);
        for (int i = 0; i < n; i++) {
            Customer customer = createCustomer(i);
            customers.add(customer);
        }
        customerRepository.save(customers);
        return n;
    }

    private static Customer createCustomer(int i) {
        Customer customer = new Customer(1L, new Date(), "C", "DT");
        customer.getContacts()
                .put(ContactType.MAIN,
                     new Contact("firstName" + i, "lastName" + i, "street" + i, Integer.toString(10000 + i),
                                 Integer.toString(i % 10), customer, ContactType.MAIN));
        return customer;
    }

    @RequestMapping("/customers/coreData/{id}")
    Customer getCoreData(@PathVariable("id") long id) {
        return customerRepository.findOne(id);
    }

}
