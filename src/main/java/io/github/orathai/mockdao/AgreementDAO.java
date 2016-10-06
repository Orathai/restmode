package io.github.orathai.mockdao;

import io.github.orathai.model.AgreementModel;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Repository
public class AgreementDAO {

    private static final List<AgreementModel> AGREEMENT_LIST = new ArrayList<AgreementModel>();

    @PostConstruct
    public void initData() {

        //create Agreement
        AgreementModel agreement1 = new AgreementModel();
        agreement1.setId(1);
        agreement1.setAgreementDetail("property loan");
        AGREEMENT_LIST.add(agreement1);

        AgreementModel agreement2 = new AgreementModel();
        agreement2.setId(2);
        agreement2.setAgreementDetail("car loan");
        AGREEMENT_LIST.add(agreement2);

    }

    public AgreementModel findByAgreementId(Long id) {

        Assert.notNull(id);
        AgreementModel result = null;

        for (AgreementModel agreement : AGREEMENT_LIST) {
            if (agreement.getId() == id) {
                result = agreement;
            }
        }
        return result;
    }

    public List<AgreementModel> findAllAgreement() {
        return AGREEMENT_LIST;
    }

    public int addAgreement(AgreementModel agreementModel) {

        if (!isAgreementExist(agreementModel.getId())) {
            AGREEMENT_LIST.add(agreementModel);
            return 1;
        }
        return 0;
    }

    public boolean isAgreementExist(Long id) {
        for (AgreementModel agreement : AGREEMENT_LIST) {
            if (agreement.getId() == id) {
                return true;
            }
        }
        return false;
    }

    public int updateAgreement(AgreementModel agreementModel) {

        for (AgreementModel agreement : AGREEMENT_LIST) {
            if (agreement.getId() == agreementModel.getId()) {

                agreement.setAgreementDetail(agreementModel.getAgreementDetail());

                int index = AGREEMENT_LIST.indexOf(agreement);
                AGREEMENT_LIST.set(index, agreementModel);
                return 1;
            }

        }
        return 0;
    }

    public int deleteAgreementById(Long id) {

        for (AgreementModel agreement : AGREEMENT_LIST) {
            if (agreement.getId() == id) {
                int index = AGREEMENT_LIST.indexOf(agreement);
                AGREEMENT_LIST.remove(index);
                //success deleting
                return 1;
            }
        }
        return 0;
    }
}
