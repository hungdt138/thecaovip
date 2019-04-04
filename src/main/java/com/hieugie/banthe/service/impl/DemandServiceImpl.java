package com.hieugie.banthe.service.impl;

import com.hieugie.banthe.domain.Demand;
import com.hieugie.banthe.repository.DemandRepository;
import com.hieugie.banthe.service.DemandService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing Demand.
 */
@Service
@Transactional
public class DemandServiceImpl implements DemandService {

    private final Logger log = LoggerFactory.getLogger(DemandServiceImpl.class);

    private final DemandRepository demandRepository;

    public DemandServiceImpl(DemandRepository demandRepository) {
        this.demandRepository = demandRepository;
    }

    /**
     * Save a demand.
     *
     * @param demand the entity to save
     * @return the persisted entity
     */
    @Override
    public Demand save(Demand demand) {
        log.debug("Request to save Demand : {}", demand);
        return demandRepository.save(demand);
    }

    /**
     * Get all the demands.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Demand> findAll(Pageable pageable) {
        log.debug("Request to get all Demands");
        return demandRepository.findAll(pageable);
    }


    /**
     * Get one demand by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Demand> findOne(Long id) {
        log.debug("Request to get Demand : {}", id);
        return demandRepository.findById(id);
    }

    /**
     * Delete the demand by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Demand : {}", id);        demandRepository.deleteById(id);
    }
}
