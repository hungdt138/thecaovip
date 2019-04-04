package com.hieugie.banthe.web.rest.dto;

public class CancelBillDTO {
    private Long partnerID;
    private String signature;

    public CancelBillDTO() {
    }

    public CancelBillDTO(Long partnerID, String signature) {
        this.partnerID = partnerID;
        this.signature = signature;
    }

    public Long getPartnerID() {
        return partnerID;
    }

    public void setPartnerID(Long partnerID) {
        this.partnerID = partnerID;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }
}
