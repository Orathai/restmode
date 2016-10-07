package io.github.orathai.mockdao;

import io.github.orathai.model.AgreementModel;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AgreementDAOTest {

    @Autowired
    private AgreementDAO agreementDAO;

    @Test
    public void findByAgreementId() throws Exception {
        AgreementModel foundAgreement = agreementDAO.findByAgreementId(1L);
        Assert.assertEquals("property loan", foundAgreement.getAgreementDetail());

    }

    @Test
    public void findAllAgreement() throws Exception {

        List<AgreementModel> agreementList = agreementDAO.findAllAgreement();
        Assert.assertEquals(2, agreementList.size());
    }

    @Ignore
    @Test
    public void addAgreement(){

        AgreementModel agreement = new AgreementModel();
        agreement.setId(3L);
        agreement.setAgreementDetail("loan");

        int result = agreementDAO.addAgreement(agreement);
        Assert.assertEquals(1, result);


    }

}