package com.hieugie.banthe.repository;

import com.hieugie.banthe.domain.DemandCharge;
import com.hieugie.banthe.domain.enumeration.NhaMang;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


/**
 * Spring Data  repository for the DemandCharge entity.
 */
@SuppressWarnings("unused")
public interface DemandChargeRepositoryCustom {
    Page<DemandCharge> search(Pageable pageable, String code, String seri);
}
