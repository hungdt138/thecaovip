package com.hieugie.banthe.service;

import com.hieugie.banthe.domain.Demand;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing Demand.
 */
public interface DemandService {

    /**
     * Save a demand.
     *
     * @param demand the entity to save
     * @return the persisted entity
     */
    Demand save(Demand demand);

    /**
     * Get all the demands.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Demand> findAll(Pageable pageable);


    /**
     * Get the "id" demand.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<Demand> findOne(Long id);

    /**
     * Delete the "id" demand.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
