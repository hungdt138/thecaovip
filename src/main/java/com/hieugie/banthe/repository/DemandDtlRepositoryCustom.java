package com.hieugie.banthe.repository;

import com.hieugie.banthe.domain.DemandDtl;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Spring Data  repository for the DemandDtl entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DemandDtlRepositoryCustom {

    List<DemandDtl> insertBulk(List<DemandDtl> demandDtls);
}
