package io.github.orathai.controller;

import io.github.orathai.mockdao.AgreementDetailDAO;
import io.github.orathai.model.AgreementDetailModel;
import io.github.orathai.model.AgreementStatus;
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
@RequestMapping("/agreementdetails")
public class AgreementDetailController {

    private static final int FAILURE_RESULT = 0;

    @Autowired
    private AgreementDetailDAO agreementDetailDAO;

    @Autowired
    private MailController mailController;

    @RequestMapping(method = RequestMethod.GET)
    public List<AgreementDetailModel> getAll() {
        return agreementDetailDAO.findAllAgreementDetail();
    }

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, value = "/{id}")
    public ResponseEntity<AgreementDetailModel> getWithId(@PathVariable Long id) {

        System.out.println("Fetching Agreement Detail with id " + id);

        AgreementDetailModel agreementDetail = agreementDetailDAO.finById(id);
        if (agreementDetail == null) {
            System.out.println("Agreement detail with id " + id + " not found");
            return new ResponseEntity<AgreementDetailModel>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<AgreementDetailModel>(agreementDetail, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, value = "/customers/{id}")
    public ResponseEntity<List<AgreementDetailModel>> getWithCustomerId(@PathVariable Long id) {

        System.out.println("Fetching Agreement detail list with customer id " + id);
        List<AgreementDetailModel> agreementDetail = agreementDetailDAO.findByCustomerId(id);

        if (agreementDetail.size() == 0) {
            System.out.println("Agreement detail list with customer id " + id + " not found");
            return new ResponseEntity<List<AgreementDetailModel>>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<List<AgreementDetailModel>>(agreementDetail, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, value = "/agreements/{id}")
    public ResponseEntity<List<AgreementDetailModel>> getWithAgreementId(@PathVariable Long id) {

        System.out.println("Fetching Agreement detail list with agreement id " + id);
        List<AgreementDetailModel> agreementDetail = agreementDetailDAO.findByAgreementId(id);

        if (agreementDetail.size() == 0) {
            System.out.println("Agreement detail list with agreement id " + id + " not found");
            return new ResponseEntity<List<AgreementDetailModel>>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<List<AgreementDetailModel>>(agreementDetail, HttpStatus.OK);
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResponseEntity<Void> createAgreementDetail(@RequestBody AgreementDetailModel agreementDetailModel,
                                                      UriComponentsBuilder ucBuilder) {

        System.out.println("Creating agreement detail from customer and agreement ");

        int result = agreementDetailDAO.addAgreementDetail(agreementDetailModel);

        if (result == FAILURE_RESULT) {
            System.out.println("ERROR: either this agreement for this customer already exist or null value");
            return new ResponseEntity<Void>(HttpStatus.CONFLICT);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/agreements/create").buildAndExpand(agreementDetailModel.getId()).toUri());
        return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public ResponseEntity<AgreementDetailModel> updateAgreement(@RequestBody AgreementDetailModel agreementDetailModel) {

        System.out.println("Updating agreement details ");

        int result = agreementDetailDAO.updateAgreementDetail(agreementDetailModel);

        if (result == FAILURE_RESULT) {
            System.out.println("ERROR: cannot find this customer or agreement");
            return new ResponseEntity<AgreementDetailModel>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<AgreementDetailModel>(HttpStatus.OK);
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<AgreementDetailModel> deleteAgreementDetail(@PathVariable("id") Long id) {

        System.out.println("Fetching & Deleting Agreement detail with id " + id);

        int result = agreementDetailDAO.deleteAgreementDetailById(id);

        if (result == FAILURE_RESULT) {
            System.out.println("Unable to delete. Agreement detail with id " + id + " not found");
            return new ResponseEntity<AgreementDetailModel>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<AgreementDetailModel>(HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value = "/mailservice", method = RequestMethod.PUT)
    public ResponseEntity<AgreementDetailModel> sendEmailToCustomer(
            @RequestBody AgreementDetailModel agreementDetailModel) {

        System.out.println("Sending an email to this customer about the agreement..");

        //Checking mail service
        boolean mailSent = mailController.send(agreementDetailModel.getCustomerModel().getCustomerEmail());

        if (mailSent) {
            agreementDetailModel.setAgreementStatus(AgreementStatus.SENT_TO_CUSTOMER);
        }
        int result = agreementDetailDAO.updateAgreementDetail(agreementDetailModel);

        if (result == FAILURE_RESULT) {
            System.out.println("ERROR: cannot find this customer or agreement");
            return new ResponseEntity<AgreementDetailModel>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<AgreementDetailModel>(HttpStatus.OK);
    }


}
