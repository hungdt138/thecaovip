package com.hieugie.banthe.service.impl;

import com.hieugie.banthe.domain.DemandDtl;
import com.hieugie.banthe.repository.DemandDtlRepository;
import com.hieugie.banthe.service.DemandDtlService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing DemandDtl.
 */
@Service
@Transactional
public class DemandDtlServiceImpl implements DemandDtlService {

    private final Logger log = LoggerFactory.getLogger(DemandDtlServiceImpl.class);

    private final DemandDtlRepository demandDtlRepository;

    public DemandDtlServiceImpl(DemandDtlRepository demandDtlRepository) {
        this.demandDtlRepository = demandDtlRepository;
    }

    /**
     * Save a demandDtl.
     *
     * @param demandDtl the entity to save
     * @return the persisted entity
     */
    @Override
    public DemandDtl save(DemandDtl demandDtl) {
        log.debug("Request to save DemandDtl : {}", demandDtl);
        return demandDtlRepository.save(demandDtl);
    }

    /**
     * Get all the demandDtls.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<DemandDtl> findAll(Pageable pageable) {
        log.debug("Request to get all DemandDtls");
        return demandDtlRepository.findAll(pageable);
    }


    /**
     * Get one demandDtl by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<DemandDtl> findOne(Long id) {
        log.debug("Request to get DemandDtl : {}", id);
        return demandDtlRepository.findById(id);
    }

    /**
     * Delete the demandDtl by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete DemandDtl : {}", id);        demandDtlRepository.deleteById(id);
    }
}
