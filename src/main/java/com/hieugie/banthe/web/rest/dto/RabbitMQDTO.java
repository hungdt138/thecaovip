package com.hieugie.banthe.web.rest.dto;

import com.hieugie.banthe.domain.DemandDtl;

import java.io.Serializable;

public class RabbitMQDTO implements Serializable {
    private ChargeRequestDTO chargeRequestDTO;
    private DemandDtl demandDtl;

    public RabbitMQDTO() {
    }

    public ChargeRequestDTO getChargeRequestDTO() {
        return chargeRequestDTO;
    }

    public void setChargeRequestDTO(ChargeRequestDTO chargeRequestDTO) {
        this.chargeRequestDTO = chargeRequestDTO;
    }

    public DemandDtl getDemandDtl() {
        return demandDtl;
    }

    public void setDemandDtl(DemandDtl demandDtl) {
        this.demandDtl = demandDtl;
    }

    @Override
    public String toString() {
        return "RabbitMQDTO{" +
            "chargeRequestDTO=" + chargeRequestDTO +
            ", demandDtl=" + demandDtl +
            '}';
    }
}
