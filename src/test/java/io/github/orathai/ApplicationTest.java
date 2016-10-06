package io.github.orathai;

import io.github.orathai.mockdao.CustomerDAO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.LinkedHashMap;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {"management.port=0"})
public class ApplicationTest {

    @LocalServerPort
    private int port;

    @Value("${management.port}")
    private int localManagerPort;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private CustomerDAO customerDAO;

    private final String REST_SERVICE_URI = "http://localhost:" + this.port+"/customers";

    @Test
    public void getAll() throws Exception {

        System.out.println("Customer lists API");

        List<LinkedHashMap<String, Object>> customersMap = testRestTemplate.getForObject(REST_SERVICE_URI, List.class);

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
    public void shouldReturn200WhenSendingRequestToController() throws Exception {


        String body = this.testRestTemplate.getForObject("/customers/1", String.class);
        assertThat(body).isEqualTo(
                "{\"id\":1,\"customerName\":\"FirstName LastName 1\",\"customerEmail\":\"cusEmail1@mail.com\"}");

        String body2 = this.testRestTemplate.getForObject("/customers", String.class);
        assertThat(body2).isEqualTo(
                "[{\"id\":1,\"customerName\":\"FirstName LastName 1\"" +
                        ",\"customerEmail\":\"cusEmail1@mail.com\"}," +
                        "{\"id\":2,\"customerName\":\"FirstName LastName 2\"" +
                        ",\"customerEmail\":\"cusEmail2@mail.com\"}," +
                        "{\"id\":3,\"customerName\":\"FirstName LastName 3\"" +
                        ",\"customerEmail\":\"cusEmail3@mail.com\"}]");



    }

}