package io.github.orathai.mockdao;

import io.github.orathai.model.AgreementDetailModel;
import io.github.orathai.model.AgreementModel;
import io.github.orathai.model.AgreementStatus;
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
public class AgreementDetailDAOTest {

    @Autowired
    private AgreementDetailDAO agreementDetailDAO;

    @Autowired
    private CustomerDAO customerDAO;

    @Autowired
    private AgreementDAO agreementDAO;

    @Test
    public void findByCustomerIdAndAgreementId() throws Exception {

        initData();

        AgreementDetailModel foundAgreementDetailModel =
                agreementDetailDAO.findByCustomerIdAndAgreementId(1L, 1L);
        Assert.assertEquals("name", foundAgreementDetailModel.getCustomerModel().getCustomerName());
        Assert.assertEquals("loan detail", foundAgreementDetailModel.getAgreementModel().getAgreementDetail());
    }

    @Test
    public void findAgreementDetailByCustomerId() throws Exception {

        initData();

        List<AgreementDetailModel> foundAgreementDetailModelList =
                agreementDetailDAO.findByCustomerId(1L);
        //Expected this customer has 2 agreements
        Assert.assertEquals(2, foundAgreementDetailModelList.size());
    }

    @Test
    public void findAgreementDetailByAgreementId() throws Exception {

        List<AgreementDetailModel> foundAgreementDetailModelList =
                agreementDetailDAO.findByAgreementId(1L);
        Assert.assertEquals(1, foundAgreementDetailModelList.size());
    }

    @Test
    public void findAllAgreementDetail() throws Exception {

        List<AgreementDetailModel> agreementDetailModelList =
                agreementDetailDAO.findAllAgreementDetail();
        Assert.assertEquals(3, agreementDetailModelList.size());
    }

    @Test
    public void updateAgreementStatus() throws Exception {

        //initData();

        AgreementDetailModel updateStatus = agreementDetailDAO.finById(2L);
        //updateStatus.setAgreementStatus(AgreementStatus.SENT_TO_CUSTOMER);

        //before
        Assert.assertEquals(AgreementStatus.PROCESSING, updateStatus.getAgreementStatus());

        //after
        updateStatus.setAgreementStatus(AgreementStatus.SENT_TO_CUSTOMER);
        agreementDetailDAO.updateAgreementDetail(updateStatus);
        Assert.assertEquals(AgreementStatus.SENT_TO_CUSTOMER, updateStatus.getAgreementStatus());
    }

    public void initData(){
        //create customer
        CustomerModel customerModel = new CustomerModel();
        customerModel.setId(1L);
        customerModel.setCustomerName("name");

        customerDAO.addCustomer(customerModel);

        //create agreement
        AgreementModel agreement = new AgreementModel();
        agreement.setId(1L);
        agreement.setAgreementDetail("loan detail");

        agreementDAO.addAgreement(agreement);

        AgreementModel agreement2 = new AgreementModel();
        agreement2.setId(2L);
        agreement2.setAgreementDetail("loan detail2");

        agreementDAO.addAgreement(agreement2);

        //set customer to AgreementDetail1
        AgreementDetailModel agreementDetailModel = new AgreementDetailModel();
        agreementDetailModel.setId(1L);
        agreementDetailModel.setCustomerModel(customerModel);
        agreementDetailModel.setAgreementModel(agreement);
        agreementDetailModel.setAgreementStatus(AgreementStatus.PROCESSING);

        agreementDetailDAO.addAgreementDetail(agreementDetailModel);

        //the same customer create new agreement2
        AgreementDetailModel agreementDetailModel2 = new AgreementDetailModel();
        agreementDetailModel2.setId(2L);
        agreementDetailModel2.setCustomerModel(customerModel);
        agreementDetailModel2.setAgreementModel(agreement2);
        agreementDetailModel2.setAgreementStatus(AgreementStatus.PROCESSING);

        agreementDetailDAO.addAgreementDetail(agreementDetailModel2);


    }

}