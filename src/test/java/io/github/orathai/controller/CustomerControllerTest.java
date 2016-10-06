package io.github.orathai.controller;

import io.github.orathai.Application;
import io.github.orathai.model.CustomerModel;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.net.URI;
import java.util.LinkedHashMap;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {"management.port=0"})
public class CustomerControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private TestRestTemplate restTemplate;

    /*@Autowired
    private CustomerDAO customerDAO;*/

    @LocalServerPort
    private int port;

    @Value("${local.server.port}")
    private int managerPort;

    @Before
    public void setup() throws Exception {

    }

    private final String REST_SERVICE_URI = "http://localhost:9000/customers";

    @Test
    public void getAll() throws Exception {

        System.out.println("Customer lists API");

        List<LinkedHashMap<String, Object>> customersMap = restTemplate.getForObject(REST_SERVICE_URI, List.class);

        if (customersMap != null) {
            for (LinkedHashMap<String, Object> map : customersMap) {
                System.out.println("Customer : id=" + map.get("id")
                        + ", Name=" + map.get("customerName")
                        + ", Email=" + map.get("customerEmail"));
            }
        } else {
            System.out.println("No customer");
        }

    }

    @Test
    public void getCustomerWithId() throws Exception {

        //GET
        System.out.println("Find customer by id");
        String customer = restTemplate.getForObject(REST_SERVICE_URI + "/1", String.class);
        System.out.println(customer);
    }

    @Test
    public void createCustomer() throws Exception {

        //POST
        System.out.println("Testing create Customer");
        CustomerModel customer = new CustomerModel();
        customer.setId(4);
        customer.setCustomerName("Will Smith");
        customer.setCustomerEmail("w@s.com");

        URI uri = restTemplate.postForLocation(REST_SERVICE_URI+"/create", customer, CustomerModel.class);
        System.out.println("Location : "+uri.toASCIIString());

    }

    @Test
    public void updateCustomer() throws Exception {

        //PUT
        System.out.println("Testing update Customer");
        CustomerModel customer  = new CustomerModel();
        customer.setId(1);
        customer.setCustomerName("Will Smith");
        customer.setCustomerEmail("w@s.com");

        restTemplate.put(REST_SERVICE_URI+"/update/1", customer);

    }

    @Test
    public void deleteCustomer() throws Exception {

        //DELETE
        System.out.println("Testing delete Customer");
        restTemplate.delete(REST_SERVICE_URI+"/delete/2");

    }

}