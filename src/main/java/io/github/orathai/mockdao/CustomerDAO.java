package io.github.orathai.mockdao;

import io.github.orathai.model.CustomerModel;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CustomerDAO {

    private static final List<CustomerModel> CUSTOMER_LIST = new ArrayList<CustomerModel>();

    @PostConstruct
    public void initData() {

        //create customer
        CustomerModel customerModel1 = new CustomerModel();
        customerModel1.setId(1);
        customerModel1.setCustomerName("FirstName LastName 1");
        customerModel1.setCustomerEmail("cusEmail1@mail.com");

        CUSTOMER_LIST.add(customerModel1);

        CustomerModel customerModel2 = new CustomerModel();
        customerModel2.setId(2);
        customerModel2.setCustomerName("FirstName LastName 2");
        customerModel2.setCustomerEmail("cusEmail2@mail.com");

        CUSTOMER_LIST.add(customerModel2);

        CustomerModel customerModel3 = new CustomerModel();
        customerModel3.setId(3);
        customerModel3.setCustomerName("FirstName LastName 3");
        customerModel3.setCustomerEmail("cusEmail3@mail.com");

        CUSTOMER_LIST.add(customerModel3);

    }

    public CustomerModel findByCustomerId(Long id) {

        Assert.notNull(id);
        CustomerModel result = null;

        for (CustomerModel customerModel : CUSTOMER_LIST) {
            if (customerModel.getId() == id) {
                result = customerModel;
            }
        }
        return result;
    }

    public List<CustomerModel> findAllCustomer() {
        return CUSTOMER_LIST;
    }

    public int addCustomer(CustomerModel customerModelModel) {

        if (!isCustomerExist(customerModelModel.getId())) {
            CUSTOMER_LIST.add(customerModelModel);
            return 1;
        }
        return 0;

    }

    public boolean isCustomerExist(Long id) {

        for (CustomerModel customer : CUSTOMER_LIST) {
            if (customer.getId() == id) {
                return true;
            }
        }
        return false;
    }

    public int updateCustomer(CustomerModel customerModel) {

        for (CustomerModel customer : CUSTOMER_LIST) {
            if (customer.getId() == customerModel.getId()) {

                customer.setCustomerName(customerModel.getCustomerName());
                customer.setCustomerEmail(customerModel.getCustomerEmail());

                int index = CUSTOMER_LIST.indexOf(customer);
                CUSTOMER_LIST.set(index, customerModel);
                return 1;
            }

        }
        return 0;
    }

    public int deleteCustomerById(Long id) {
        for (CustomerModel customer : CUSTOMER_LIST) {
            if (customer.getId() == id) {
                int index = CUSTOMER_LIST.indexOf(customer);
                CUSTOMER_LIST.remove(index);
                return 1;
            }
        }
        return 0;
    }
}
