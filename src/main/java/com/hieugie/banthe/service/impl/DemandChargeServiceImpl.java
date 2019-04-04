package com.hieugie.banthe.service.impl;

import com.hieugie.banthe.domain.DemandCharge;
import com.hieugie.banthe.domain.enumeration.NhaMang;
import com.hieugie.banthe.repository.DemandChargeRepository;
import com.hieugie.banthe.service.DemandChargeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing DemandCharge.
 */
@Service
@Transactional
public class DemandChargeServiceImpl implements DemandChargeService {

    private final Logger log = LoggerFactory.getLogger(DemandChargeServiceImpl.class);

    private final DemandChargeRepository demandChargeRepository;

    public DemandChargeServiceImpl(DemandChargeRepository demandChargeRepository) {
        this.demandChargeRepository = demandChargeRepository;
    }

    /**
     * Save a demandCharge.
     *
     * @param demandCharge the entity to save
     * @return the persisted entity
     */
    @Override
    public DemandCharge save(DemandCharge demandCharge) {
        log.debug("Request to save DemandCharge : {}", demandCharge);
        return demandChargeRepository.save(demandCharge);
    }

    /**
     * Get all the demandCharges.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<DemandCharge> findAll(Pageable pageable) {
        log.debug("Request to get all DemandCharges");
        return demandChargeRepository.findByStatusAndType(pageable, 0, NhaMang.VTT);
    }


    /**
     * Get one demandCharge by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<DemandCharge> findOne(Long id) {
        log.debug("Request to get DemandCharge : {}", id);
        return demandChargeRepository.findById(id);
    }

    /**
     * Delete the demandCharge by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete DemandCharge : {}", id);        demandChargeRepository.deleteById(id);
    }

    @Override
    public Page<DemandCharge> findAllHand(Pageable pageable) {
        return demandChargeRepository.findByStatusAndTypeNot(pageable, 0, NhaMang.VTT);
    }

    @Override
    public Page<DemandCharge> search(Pageable pageable, String code, String seri) {
        return demandChargeRepository.search(pageable, code, seri);
    }
}
