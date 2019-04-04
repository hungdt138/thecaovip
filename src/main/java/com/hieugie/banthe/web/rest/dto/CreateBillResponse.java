package com.hieugie.banthe.web.rest.dto;

public class CreateBillResponse {
    private Integer errorCode;
    private String errorMsg;
    private Long partnerID;

    public CreateBillResponse() {
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public Long getPartnerID() {
        return partnerID;
    }

    public void setPartnerID(Long partnerID) {
        this.partnerID = partnerID;
    }

    @Override
    public String toString() {
        return "CreateBillResponse{" +
            "errorCode=" + errorCode +
            ", errorMsg='" + errorMsg + '\'' +
            ", partnerID=" + partnerID +
            '}';
    }
}
