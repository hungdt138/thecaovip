package com.hieugie.banthe.web.rest.dto;

import java.math.BigDecimal;

public class ReportDTO {
    private String user;
    private BigDecimal realAmount;
    private BigDecimal chargedAmount;
    private BigDecimal afterFeeAmount;
    private BigDecimal prePay;
    private BigDecimal afterPay;

    public ReportDTO() {
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public BigDecimal getChargedAmount() {
        return chargedAmount;
    }

    public void setChargedAmount(BigDecimal chargedAmount) {
        this.chargedAmount = chargedAmount;
    }

    public BigDecimal getAfterFeeAmount() {
        return afterFeeAmount;
    }

    public void setAfterFeeAmount(BigDecimal afterFeeAmount) {
        this.afterFeeAmount = afterFeeAmount;
    }

    public ReportDTO(String user, BigDecimal chargedAmount, BigDecimal afterFeeAmount) {
        this.user = user;
        this.chargedAmount = chargedAmount;
        this.afterFeeAmount = afterFeeAmount;
    }

    public BigDecimal getRealAmount() {
        return realAmount;
    }

    public void setRealAmount(BigDecimal realAmount) {
        this.realAmount = realAmount;
    }

    public BigDecimal getPrePay() {
        return prePay;
    }

    public void setPrePay(BigDecimal prePay) {
        this.prePay = prePay;
    }

    public BigDecimal getAfterPay() {
        return afterPay;
    }

    public void setAfterPay(BigDecimal afterPay) {
        this.afterPay = afterPay;
    }
}
