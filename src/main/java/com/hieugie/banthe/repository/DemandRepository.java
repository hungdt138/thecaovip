package com.hieugie.banthe.repository;

import com.hieugie.banthe.domain.Demand;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the Demand entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DemandRepository extends JpaRepository<Demand, Long>, DemandRepositoryCustom {
    List<Demand> findByBillId(Long id);

    @Modifying
    @Query(value = "update demand set status = 1 where id = :id", nativeQuery = true)
    void updateInvalidPhoneNumber(@Param("id") Long id);

    @Modifying
    @Query(value = "update demand set status = 2, invalid_date = STR_TO_DATE(:exceedDate, '%Y%m%d') where id = :id", nativeQuery = true)
    void updateChargeExceed(@Param("id") Long id, @Param("exceedDate") String exceedDate);

    @Modifying
    @Query(value = "update demand set charged_amount = charged_amount + :chargedAmount where id = :id", nativeQuery = true)
    void updateChargeAmount(@Param("chargedAmount") Long chargedAmount, @Param("id") Long id);
}
