package io.github.orathai.controller;

import io.github.orathai.mockdao.CustomerDAO;
import io.github.orathai.model.CustomerModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    private static final int FAILURE_RESULT = 0;

    @Autowired
    private CustomerDAO customerDAO;

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CustomerModel>> getAll() {
        return new ResponseEntity<List<CustomerModel>>(customerDAO.findAllCustomer(), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CustomerModel> getCustomerWithId(@PathVariable Long id) {

        System.out.println("Fetching Customer with id " + id);

        CustomerModel customer = customerDAO.findByCustomerId(id);
        if (customer == null) {
            System.out.println("Customer with id " + id + " not found");
            return new ResponseEntity<CustomerModel>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<CustomerModel>(customer, HttpStatus.OK);
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResponseEntity<Void> createCustomer(@RequestBody CustomerModel customer,
                                               UriComponentsBuilder ucBuilder) {
        System.out.println("Creating Customer " + customer.getCustomerName());

        int result = customerDAO.addCustomer(customer);
        if(result == FAILURE_RESULT){
            System.out.println("A Customer with name " + customer.getCustomerName() + " already exist");
            return new ResponseEntity<Void>(HttpStatus.CONFLICT);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/customers/create").buildAndExpand(customer.getId()).toUri());
        return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/update/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Void> updateCustomer(@PathVariable("id") Long id,
                                                        @RequestBody CustomerModel customerModel) {
        System.out.println("Updating Customer " + id);

        int result = customerDAO.updateCustomer(customerModel);
        if(result == FAILURE_RESULT){
            System.out.println("Customer with id " + id + " not found");
            return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<CustomerModel> deleteCustomer(@PathVariable("id") Long id) {

        System.out.println("Fetching & Deleting Customer with id " + id);

        int result = customerDAO.deleteCustomerById(id);
        if(result == FAILURE_RESULT){
            System.out.println("Unable to delete. User with id " + id + " not found");
            return new ResponseEntity<CustomerModel>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<CustomerModel>(HttpStatus.NO_CONTENT);
    }
}
