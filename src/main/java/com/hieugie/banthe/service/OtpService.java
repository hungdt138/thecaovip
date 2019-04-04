package com.hieugie.banthe.service;

import com.hieugie.banthe.domain.Otp;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing Otp.
 */
public interface OtpService {

    /**
     * Save a otp.
     *
     * @param otp the entity to save
     * @return the persisted entity
     */
    Otp save(Otp otp);

    /**
     * Get all the otps.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Otp> findAll(Pageable pageable);


    /**
     * Get the "id" otp.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<Otp> findOne(Long id);

    /**
     * Delete the "id" otp.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
