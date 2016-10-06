package io.github.orathai.mockdao;

import io.github.orathai.model.CustomerModel;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CustomerDAOTest {

    @Autowired
    private CustomerDAO customerDAO;

    @Test
    public void findByCustomerId() throws Exception {

        CustomerModel customerModel = new CustomerModel();
        customerModel.setId(4L);
        customerModel.setCustomerName("name");
        customerModel.setCustomerEmail("email@mail.com");

        customerDAO.addCustomer(customerModel);
        CustomerModel foundCustomerModel = customerDAO.findByCustomerId(4L);

        Assert.assertEquals("name", foundCustomerModel.getCustomerName());
    }

    @Test
    public void findAllCustomer() throws Exception {

        List<CustomerModel> customerModelList = customerDAO.findAllCustomer();
        Assert.assertEquals(3, customerModelList.size());
    }

}