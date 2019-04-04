package com.hieugie.banthe.web.rest.dto;

import java.math.BigDecimal;

public class DemandChargeDTO {

    private Long id;
    private BigDecimal realValue;
    private Integer status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getRealValue() {
        return realValue;
    }

    public void setRealValue(BigDecimal realValue) {
        this.realValue = realValue;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public DemandChargeDTO() {
    }

    public DemandChargeDTO(Long id, BigDecimal realValue, Integer status) {
        this.id = id;
        this.realValue = realValue;
        this.status = status;
    }
}
