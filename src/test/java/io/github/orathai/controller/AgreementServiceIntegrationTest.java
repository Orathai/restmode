package io.github.orathai.controller;

import io.github.orathai.Application;
import io.github.orathai.model.Deal;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;


/*
* Integration test, need to run the application before running the test cases
* */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {"management.port=0"})
public class AgreementServiceIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    @Value("${local.server.port}")
    private int managerPort;

    private final String REST_SERVICE_URI = "http://localhost:9000/agreementservices";

    //Good day test case
    @Test
    public void createAgreementDetailOK() throws Exception {

        Deal deal = new Deal();
        deal.setCustomerName("Peter Pan");
        deal.setCustomerEmail("p@p.com");
        deal.setDealDetail("Vacation Loan");

        ResponseEntity<String> response = restTemplate.postForEntity(REST_SERVICE_URI+"/create", deal, String.class);
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());

    }

    //Bad day test case
    @Test
    public void nullOrEmptyEmail() throws Exception {

        Deal deal = new Deal();
        {
            deal.setCustomerName("Peter Pan");
            deal.setCustomerEmail(null);
            deal.setDealDetail("Vacation Loan");

            ResponseEntity<String> response = restTemplate.postForEntity(REST_SERVICE_URI+"/create", deal, String.class);
            Assert.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        }
        {
            deal.setCustomerName("Peter Pan");
            deal.setCustomerEmail(" ");
            deal.setDealDetail("Vacation Loan");

            ResponseEntity<String> response = restTemplate.postForEntity(REST_SERVICE_URI+"/create", deal, String.class);
            Assert.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        }

    }

    @Test
    public void nullOrEmptyDealDetail() throws Exception {

        Deal deal = new Deal();
        {
            deal.setCustomerName("Peter Pan");
            deal.setCustomerEmail("p@p");
            deal.setDealDetail(null);

            ResponseEntity<String> response = restTemplate.postForEntity(REST_SERVICE_URI+"/create", deal, String.class);
            Assert.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        }
        {
            deal.setCustomerName("Peter Pan");
            deal.setCustomerEmail("p@p");
            deal.setDealDetail("    ");

            ResponseEntity<String> response = restTemplate.postForEntity(REST_SERVICE_URI+"/create", deal, String.class);
            Assert.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        }

    }

}