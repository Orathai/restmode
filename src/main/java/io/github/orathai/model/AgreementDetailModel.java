package io.github.orathai.model;

public class AgreementDetailModel {

    private long id;
    private CustomerModel customerModel;
    private AgreementModel agreementModel;
    private AgreementStatus agreementStatus;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public CustomerModel getCustomerModel() {
        return customerModel;
    }

    public void setCustomerModel(CustomerModel customerModelModel) {
        this.customerModel = customerModelModel;
    }

    public AgreementModel getAgreementModel() {
        return agreementModel;
    }

    public void setAgreementModel(AgreementModel agreementModel) {
        this.agreementModel = agreementModel;
    }

    public AgreementStatus getAgreementStatus() {
        return agreementStatus;
    }

    public void setAgreementStatus(AgreementStatus agreementStatus) {
        this.agreementStatus = agreementStatus;
    }
}
