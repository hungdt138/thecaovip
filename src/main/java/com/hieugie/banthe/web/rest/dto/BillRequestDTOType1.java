package com.hieugie.banthe.web.rest.dto;

import java.util.List;

public class BillRequestDTOType1{
    private List<DemandRequestDTO> data;
    private Integer type;
    private Long tranID;
    private String issuer;
    private String signature;
    private String clientIP;

    public BillRequestDTOType1() {
        this.clientIP = "172.10.16.69";
    }

    public BillRequestDTOType1(Integer type, Long tranID, String issuer, String signature) {
        this.type = type;
        this.tranID = tranID;
        this.issuer = issuer;
        this.signature = signature;
        this.clientIP = "172.16.10.69";
    }

    public List<DemandRequestDTO> getData() {
        return data;
    }

    public void setData(List<DemandRequestDTO> data) {
        this.data = data;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Long getTranID() {
        return tranID;
    }

    public void setTranID(Long tranID) {
        this.tranID = tranID;
    }

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getClientIP() {
        return clientIP;
    }

    public void setClientIP(String clientIP) {
        this.clientIP = clientIP;
    }
}
