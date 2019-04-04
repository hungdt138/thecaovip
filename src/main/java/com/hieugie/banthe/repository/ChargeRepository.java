package com.hieugie.banthe.repository;

import com.hieugie.banthe.domain.Demand;
import com.hieugie.banthe.domain.DemandDtl;
import com.hieugie.banthe.domain.enumeration.NhaMang;

import java.math.BigDecimal;

public interface ChargeRepository {
    DemandDtl getChargeCard(NhaMang issuer, BigDecimal price);

    Demand getChargeCardNovalue(NhaMang issuer, BigDecimal inputValue, Long id);
}
