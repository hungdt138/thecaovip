package com.hieugie.banthe.web.rest.dto;

import java.math.BigDecimal;
import java.util.List;

public class BillRequestDTOType2 extends BillRequestDTO{
    private BigDecimal data;
    private Integer type;
    private Long tranID;
    private String issuer;
    private String signature;
    private String clientIP;

    public BillRequestDTOType2() {
        this.clientIP = "172.10.16.69";
    }

    public BillRequestDTOType2(Integer type, Long tranID, String issuer, String signature) {
        this.type = type;
        this.tranID = tranID;
        this.issuer = issuer;
        this.signature = signature;
        this.clientIP = "172.16.10.69";
    }

    public BigDecimal getData() {
        return data;
    }

    public void setData(BigDecimal data) {
        this.data = data;
    }

    @Override
    public Integer getType() {
        return type;
    }

    @Override
    public void setType(Integer type) {
        this.type = type;
    }

    @Override
    public Long getTranID() {
        return tranID;
    }

    @Override
    public void setTranID(Long tranID) {
        this.tranID = tranID;
    }

    @Override
    public String getIssuer() {
        return issuer;
    }

    @Override
    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    @Override
    public String getSignature() {
        return signature;
    }

    @Override
    public void setSignature(String signature) {
        this.signature = signature;
    }

    @Override
    public String getClientIP() {
        return clientIP;
    }

    @Override
    public void setClientIP(String clientIP) {
        this.clientIP = clientIP;
    }
}
