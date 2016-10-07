package io.github.orathai.controller;

import io.github.orathai.mockdao.AgreementDAO;
import io.github.orathai.model.AgreementModel;
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
@RequestMapping("/agreements")
public class AgreementController {

    private static final int FAILURE_RESULT = 0;

    @Autowired
    private AgreementDAO agreementDAO;

    //curl -s http://localhost:9000/agreements
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<AgreementModel>> getAll() {
        return new ResponseEntity<List<AgreementModel>>(agreementDAO.findAllAgreement(), HttpStatus.OK);
    }

    //curl -s http://localhost:9000/agreements/{id}
    @RequestMapping(method = RequestMethod.GET, value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AgreementModel> getAgreementWithId(@PathVariable Long id) {
        System.out.println("Fetching Agreement with id " + id);

        AgreementModel agreement = agreementDAO.findByAgreementId(id);
        if (agreement == null) {
            System.out.println("Agreement with id " + id + " not found");
            return new ResponseEntity<AgreementModel>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<AgreementModel>(agreement, HttpStatus.OK);
    }

    //
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResponseEntity<Void> createAgreement(@RequestBody AgreementModel agreementModel,
                                                UriComponentsBuilder ucBuilder) {
        System.out.println("Creating Agreement " + agreementModel.getAgreementDetail());

        int result = agreementDAO.addAgreement(agreementModel);
        if (result == FAILURE_RESULT) {
            System.out.println("An agreement " + agreementModel.getAgreementDetail() + " already exist");
            return new ResponseEntity<Void>(HttpStatus.CONFLICT);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/agreements/create").buildAndExpand(agreementModel.getId()).toUri());
        return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/update/{id}", method = RequestMethod.PUT)
    public ResponseEntity<AgreementModel> updateAgreement(@PathVariable("id") Long id,
                                                          @RequestBody AgreementModel agreementModel) {
        System.out.println("Updating Agreement " + id);

        int result = agreementDAO.updateAgreement(agreementModel);
        if (result == FAILURE_RESULT) {
            System.out.println("Agreement with id " + id + " not found");
            return new ResponseEntity<AgreementModel>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<AgreementModel>(HttpStatus.OK);
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<AgreementModel> deleteAgreement(@PathVariable("id") Long id) {

        System.out.println("Fetching & Deleting Agreement with id " + id);

        int result = agreementDAO.deleteAgreementById(id);
        if (result == FAILURE_RESULT) {
            System.out.println("Unable to delete. Agreement with id " + id + " not found");
            return new ResponseEntity<AgreementModel>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<AgreementModel>(HttpStatus.NO_CONTENT);
    }
}
