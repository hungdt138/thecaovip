package com.hieugie.banthe.repository;

import com.hieugie.banthe.domain.DemandCharge;
import com.hieugie.banthe.domain.enumeration.NhaMang;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


/**
 * Spring Data  repository for the DemandCharge entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DemandChargeRepository extends JpaRepository<DemandCharge, Long>, DemandChargeRepositoryCustom {
    @Modifying
    @Query(value = "UPDATE DEMAND_DTL SET charge_quantity = charge_quantity + 1 where id = :id",nativeQuery = true)
    void updateChargedQuantity(@Param("id") Long id);

    @Query(value = "select * from demand_charge where code = ?1 and status != 2", nativeQuery = true)
    Optional<DemandCharge> findByCode(String code);

    Page<DemandCharge> findByStatus(Pageable pageable, int status);

    Page<DemandCharge> findByStatusAndType(Pageable pageable, int i, NhaMang vtt);
    
    Page<DemandCharge> findByStatusAndTypeNot(Pageable pageable, int i, NhaMang vtt);

    List<DemandCharge> findByPartnerId(Long partnerId);

    @Query(value = "select * from demand_charge where start_date is not null and partner_id = :id order by last_modified_date desc limit 4", nativeQuery = true)
    List<DemandCharge> countByPartnerId(@Param("id") Long partnerId);
}
