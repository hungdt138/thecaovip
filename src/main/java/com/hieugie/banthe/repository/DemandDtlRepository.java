package com.hieugie.banthe.repository;

import com.hieugie.banthe.domain.DemandDtl;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;


/**
 * Spring Data  repository for the DemandDtl entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DemandDtlRepository extends JpaRepository<DemandDtl, Long>, DemandDtlRepositoryCustom {

    DemandDtl findByDemandIdAndPrice(Long id, BigDecimal inputValue);
}
