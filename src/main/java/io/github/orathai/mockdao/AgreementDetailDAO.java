package io.github.orathai.mockdao;

import io.github.orathai.model.AgreementModel;
import io.github.orathai.model.AgreementDetailModel;
import io.github.orathai.model.AgreementStatus;
import io.github.orathai.model.CustomerModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Repository
public class AgreementDetailDAO {

    private static final List<AgreementDetailModel> AGREEMENT_DETAIL_LIST = new ArrayList<AgreementDetailModel>();

    @Autowired
    private CustomerDAO customerDAO;

    @Autowired
    private AgreementDAO agreementDAO;

    @PostConstruct
    public void initData() {

        //Get customer from CustomerRepository
        List<CustomerModel> customerModelList = customerDAO.findAllCustomer();
        CustomerModel customerModel1 = customerModelList.get(0);
        CustomerModel customerModel2 = customerModelList.get(1);

        //get Agreement from AgreementRepository
        List<AgreementModel> agreementList = agreementDAO.findAllAgreement();
        AgreementModel agreement1 = agreementList.get(0);
        AgreementModel agreement2 = agreementList.get(1);

        //create Agreement Detail
        AgreementDetailModel agreementDetailModel1 = new AgreementDetailModel();
        agreementDetailModel1.setId(1);
        agreementDetailModel1.setAgreementModel(agreement1);
        agreementDetailModel1.setCustomerModel(customerModel1);
        agreementDetailModel1.setAgreementStatus(AgreementStatus.PROCESSING);

        AGREEMENT_DETAIL_LIST.add(agreementDetailModel1);

        AgreementDetailModel agreementDetailModel2 = new AgreementDetailModel();
        agreementDetailModel2.setId(2);
        agreementDetailModel2.setAgreementModel(agreement2);
        agreementDetailModel2.setCustomerModel(customerModel1);
        agreementDetailModel2.setAgreementStatus(AgreementStatus.PROCESSING);

        AGREEMENT_DETAIL_LIST.add(agreementDetailModel2);

        AgreementDetailModel agreementDetailModel3 = new AgreementDetailModel();
        agreementDetailModel3.setId(3);
        agreementDetailModel3.setAgreementModel(agreement2);
        agreementDetailModel3.setCustomerModel(customerModel2);
        agreementDetailModel3.setAgreementStatus(AgreementStatus.PROCESSING);

        AGREEMENT_DETAIL_LIST.add(agreementDetailModel3);

    }

    public AgreementDetailModel findByCustomerIdAndAgreementId(Long customerId,
                                                               Long agreementId) {

        Assert.notNull(customerId);
        Assert.notNull(agreementId);
        AgreementDetailModel result = null;

        for (AgreementDetailModel agreementDetailModel : AGREEMENT_DETAIL_LIST) {
            if ((agreementDetailModel.getCustomerModel().getId() == customerId) &&
                    (agreementDetailModel.getAgreementModel().getId() == agreementId)) {

                result = agreementDetailModel;
            }

        }
        return result;
    }

    public List<AgreementDetailModel> findByCustomerId(Long customerId) {

        Assert.notNull(customerId);
        List<AgreementDetailModel> result = new ArrayList<>();

        for (AgreementDetailModel agreementDetailModel : AGREEMENT_DETAIL_LIST) {
            if (agreementDetailModel.getCustomerModel().getId() == customerId) {
                result.add(agreementDetailModel);
            }
        }
        return result;
    }

    public List<AgreementDetailModel> findByAgreementId(Long agreementId) {

        Assert.notNull(agreementId);
        List<AgreementDetailModel> result = new ArrayList<>();

        for (AgreementDetailModel agreementDetailModel : AGREEMENT_DETAIL_LIST) {
            if (agreementDetailModel.getAgreementModel().getId() == agreementId) {
                result.add(agreementDetailModel);
            }
        }
        return result;
    }

    public List<AgreementDetailModel> findAllAgreementDetail() {
        return AGREEMENT_DETAIL_LIST;
    }

    public int addAgreementDetail(AgreementDetailModel agreementDetailModel) {

        //Check customer and agreement
        CustomerModel customer = customerDAO.findByCustomerId(agreementDetailModel.getCustomerModel().getId());
        AgreementModel agreement = agreementDAO.findByAgreementId(agreementDetailModel.getAgreementModel().getId());

        if (customer != null && agreement != null) {

            //Check if this customer already has this agreement
            boolean isDuplicate =
                    isAgreementDetailDuplicate(
                            agreementDetailModel.getCustomerModel().getId(),
                            agreementDetailModel.getAgreementModel().getId());

            if (!isDuplicate) {
                AGREEMENT_DETAIL_LIST.add(agreementDetailModel);
                return 1;
            }

        }
        return 0;
    }

    public int updateAgreementDetail(AgreementDetailModel agreementDetailModel) {

        for (AgreementDetailModel agreementDetail : AGREEMENT_DETAIL_LIST) {

            if (agreementDetail.getId() == agreementDetailModel.getId()) {

                agreementDetail.setCustomerModel(agreementDetailModel.getCustomerModel());
                agreementDetail.setAgreementModel(agreementDetailModel.getAgreementModel());
                agreementDetail.setAgreementStatus(agreementDetailModel.getAgreementStatus());

                int index = AGREEMENT_DETAIL_LIST.indexOf(agreementDetail);
                AGREEMENT_DETAIL_LIST.set(index, agreementDetailModel);
                return 1;
            }
        }
        return 0;
    }

    public AgreementDetailModel finById(Long agreementDetailId) {
        Assert.notNull(agreementDetailId);
        AgreementDetailModel result = null;

        for (AgreementDetailModel agreementDetailModel : AGREEMENT_DETAIL_LIST) {
            if (agreementDetailModel.getId() == agreementDetailId) {

                result = agreementDetailModel;
            }

        }
        return result;
    }

    public boolean isAgreementDetailExist(Long id) {

        for (AgreementDetailModel agreementDetail : AGREEMENT_DETAIL_LIST) {
            if (agreementDetail.getId() == id) {
                return true;
            }
        }
        return false;
    }

    public boolean isAgreementDetailDuplicate(Long customerId, Long agreementId) {

        for (AgreementDetailModel agreementDetail : AGREEMENT_DETAIL_LIST) {

            if ((agreementDetail.getCustomerModel().getId() == customerId) &&
                    (agreementDetail.getAgreementModel().getId() == agreementId)) {

                return true;
            }
        }
        return false;
    }

    public int deleteAgreementDetailById(Long id) {

        for (AgreementDetailModel agreementDetail : AGREEMENT_DETAIL_LIST) {
            if (agreementDetail.getId() == id) {
                int index = AGREEMENT_DETAIL_LIST.indexOf(agreementDetail);
                AGREEMENT_DETAIL_LIST.remove(index);
                //success deleting
                return 1;
            }
        }
        return 0;
    }
}
