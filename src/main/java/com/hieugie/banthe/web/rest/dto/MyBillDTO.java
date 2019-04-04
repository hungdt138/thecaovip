package com.hieugie.banthe.web.rest.dto;

import java.math.BigDecimal;

public class MyBillDTO {
    private String name;
    private BigDecimal amount;
    private String type;
    private BigDecimal chargedAmount;
    private Integer priority;
    private Integer chargeType;
    private Long partnerId;
    private Long id;
    private String account;
    private Integer status;
    private String fullName;
    private String createdDate;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public BigDecimal getChargedAmount() {
        return chargedAmount;
    }

    public void setChargedAmount(BigDecimal chargedAmount) {
        this.chargedAmount = chargedAmount;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Integer getChargeType() {
        return chargeType;
    }

    public void setChargeType(Integer chargeType) {
        this.chargeType = chargeType;
    }

    public MyBillDTO(){
    }

    public MyBillDTO(String name, BigDecimal amount, String type, BigDecimal chargedAmount, Integer priority, Integer chargeType) {
        this.name = name;
        this.amount = amount;
        this.type = type;
        this.chargedAmount = chargedAmount;
        this.priority = priority;
        this.chargeType = chargeType;
    }

    public Long getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(Long partnerId) {
        this.partnerId = partnerId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }
}
