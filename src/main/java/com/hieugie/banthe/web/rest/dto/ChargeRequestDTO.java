package com.hieugie.banthe.web.rest.dto;

import com.hieugie.banthe.domain.enumeration.NhaMang;

import java.io.Serializable;
import java.math.BigDecimal;

public class ChargeRequestDTO implements Serializable {
    private Long id;
    private Long partnerId;
    private String requestId;
    private String code;
    private String serial;
    private NhaMang issuer;
    private BigDecimal inputValue;
    private String signature;

    public ChargeRequestDTO() {
    }

    public ChargeRequestDTO(Long partnerId, String requestId, String code, String serial, NhaMang issuer, BigDecimal inputValue, String signature) {
        this.partnerId = partnerId;
        this.requestId = requestId;
        this.code = code;
        this.serial = serial;
        this.issuer = issuer;
        this.inputValue = inputValue;
        this.signature = signature;
    }

    public Long getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(Long partnerId) {
        this.partnerId = partnerId;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public NhaMang getIssuer() {
        return issuer;
    }

    public void setIssuer(NhaMang issuer) {
        this.issuer = issuer;
    }

    public BigDecimal getInputValue() {
        return inputValue;
    }

    public void setInputValue(BigDecimal inputValue) {
        this.inputValue = inputValue;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
