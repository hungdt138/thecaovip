package com.hieugie.banthe.service;

import com.hieugie.banthe.domain.DemandCharge;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing DemandCharge.
 */
public interface DemandChargeService {

    /**
     * Save a demandCharge.
     *
     * @param demandCharge the entity to save
     * @return the persisted entity
     */
    DemandCharge save(DemandCharge demandCharge);

    /**
     * Get all the demandCharges.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<DemandCharge> findAll(Pageable pageable);


    /**
     * Get the "id" demandCharge.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<DemandCharge> findOne(Long id);

    /**
     * Delete the "id" demandCharge.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    Page<DemandCharge> findAllHand(Pageable pageable);

    Page<DemandCharge> search(Pageable pageable, String code, String seri);
}
