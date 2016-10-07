package io.github.orathai.controller;

import io.github.orathai.mockdao.AgreementDAO;
import io.github.orathai.mockdao.AgreementDetailDAO;
import io.github.orathai.mockdao.CustomerDAO;
import io.github.orathai.model.AgreementDetailModel;
import io.github.orathai.model.AgreementModel;
import io.github.orathai.model.AgreementStatus;
import io.github.orathai.model.CustomerModel;
import io.github.orathai.model.Deal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/agreementservices")
public class AgreementService {

    @Autowired
    private CustomerDAO customerDAO;

    @Autowired
    private AgreementDAO agreementDAO;

    @Autowired
    private AgreementDetailDAO agreementDetailDAO;

    @Autowired
    private MailController mailController;

    private static final int FAILURE_RESULT = 0;

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResponseEntity<Void> createAgreementDetail(UriComponentsBuilder ucBuilder,
                                                      @RequestBody Deal deal) {

        if (checkEmptyValue(deal)) return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);

        System.out.println("Creating new customer");
        CustomerModel customer = createCustomer(deal.getCustomerName(), deal.getCustomerEmail());

        System.out.println("Creating new agreement");
        AgreementModel agreement = createAgreement(deal.getDealDetail());

        System.out.println("Create deal between customer and agreement");
        AgreementDetailModel agreementDetail = createAgreementDetail(customer, agreement);

        int result = agreementDetailDAO.addAgreementDetail(agreementDetail);

        if (result == FAILURE_RESULT) {
            System.out.println("ERROR: either this agreement for this customer already exist or null value");
            return new ResponseEntity<Void>(HttpStatus.CONFLICT);
        }

        System.out.println("Sending an email to this customer about the deal and agreement..");
        //Checking mail service
        boolean mailSent = mailController.send(agreementDetail.getCustomerModel().getCustomerEmail());
        if (!mailSent) {
            System.out.println("Unable to send an email");
            return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
        }

        //Update new status(SENT_TO_CUSTOMER) into AgreementDetail
        agreementDetail.setAgreementStatus(AgreementStatus.SENT_TO_CUSTOMER);
        int statusResult = agreementDetailDAO.updateAgreementDetail(agreementDetail);

        if (statusResult == FAILURE_RESULT) {
            System.out.println("ERROR: cannot find this customer or agreement");
            return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/agreementservices/create")
                .buildAndExpand(agreementDetail.getId()).toUri());

        System.out.println("done..");

        return new ResponseEntity<Void>(headers, HttpStatus.OK);
    }

    private boolean checkEmptyValue(@RequestBody Deal deal) {
        boolean isDealEmpty = deal.getDealDetail() == null || deal.getDealDetail().trim().length() == 0;
        if (isDealEmpty) {
            System.out.println("cannot create null agreement");
            return true;
        }
        boolean isEmailEmpty = deal.getCustomerEmail() == null || deal.getCustomerEmail().trim().length() == 0;
        if (isEmailEmpty) {
            System.out.println("cannot create null email");
            return true;
        }
        return false;
    }

    private CustomerModel createCustomer(String name, String email) {
        CustomerModel customer = new CustomerModel();
        customer.setId(4);
        customer.setCustomerName(name);
        customer.setCustomerEmail(email);

        customerDAO.addCustomer(customer);
        return customer;
    }

    private AgreementModel createAgreement(String detail) {
        AgreementModel agreement = new AgreementModel();
        agreement.setId(3);
        agreement.setAgreementDetail(detail);

        agreementDAO.addAgreement(agreement);
        return agreement;
    }

    private AgreementDetailModel createAgreementDetail(CustomerModel customerModel,
                                                       AgreementModel agreementModel) {
        AgreementDetailModel agreementDetail = new AgreementDetailModel();
        agreementDetail.setId(10);
        agreementDetail.setAgreementModel(agreementModel);
        agreementDetail.setCustomerModel(customerModel);
        agreementDetail.setAgreementStatus(AgreementStatus.PROCESSING);
        return agreementDetail;
    }
}
