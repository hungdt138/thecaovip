package com.hieugie.banthe.repository;

import com.hieugie.banthe.domain.Bill;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Bill entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BillRepository extends JpaRepository<Bill, Long>, BillRepositoryCustom {

    @Modifying
    @Query(value = "UPDATE bill SET STATUS = 0 where STATUS = 1 AND ID = ?1", nativeQuery = true)
    void updateStatus(Long id);

    @Modifying
    @Query(value = "update bill set charged_amount = charged_amount + :chargedAmount where id = :id", nativeQuery = true)
    void updateChargeAmount(@Param("chargedAmount") Long chargedAmount, @Param("id") Long id);

    @Query(value = "SELECT IFNULL(CHARGE_ERROR, 0) FROM bill WHERE id = :id", nativeQuery = true)
    int findChargeError(@Param("id") Long id);

    Bill findByPartnerId(Long partnerId);
}
