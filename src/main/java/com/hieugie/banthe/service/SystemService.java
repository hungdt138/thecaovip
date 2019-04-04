package com.hieugie.banthe.service;

import com.hieugie.banthe.domain.System;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing System.
 */
public interface SystemService {

    /**
     * Save a system.
     *
     * @param system the entity to save
     * @return the persisted entity
     */
    System save(System system);

    /**
     * Get all the systems.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Optional<System> findAll(Pageable pageable);


    /**
     * Get the "id" system.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<System> findOne(Long id);

    /**
     * Delete the "id" system.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    System findByStatusTrue();
}
