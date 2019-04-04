package com.hieugie.banthe.service;

import com.hieugie.banthe.domain.DemandDtl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing DemandDtl.
 */
public interface DemandDtlService {

    /**
     * Save a demandDtl.
     *
     * @param demandDtl the entity to save
     * @return the persisted entity
     */
    DemandDtl save(DemandDtl demandDtl);

    /**
     * Get all the demandDtls.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<DemandDtl> findAll(Pageable pageable);


    /**
     * Get the "id" demandDtl.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<DemandDtl> findOne(Long id);

    /**
     * Delete the "id" demandDtl.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
