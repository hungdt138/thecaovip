package com.hieugie.banthe.service.impl;

import com.hieugie.banthe.domain.System;
import com.hieugie.banthe.domain.User;
import com.hieugie.banthe.repository.SystemRepository;
import com.hieugie.banthe.service.SystemService;
import com.hieugie.banthe.service.UserService;
import com.hieugie.banthe.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing System.
 */
@Service
@Transactional
public class SystemServiceImpl implements SystemService {

    private final Logger log = LoggerFactory.getLogger(SystemServiceImpl.class);

    private final SystemRepository systemRepository;

    private final UserService userService;

    public SystemServiceImpl(SystemRepository systemRepository, UserService userService) {
        this.systemRepository = systemRepository;
        this.userService = userService;
    }

    /**
     * Save a system.
     *
     * @param system the entity to save
     * @return the persisted entity
     */
    @Override
    public System save(System system) {
        log.debug("Request to save System : {}", system);
        system.setStatus(true);
        systemRepository.updateStatus();
        system.setId(null);
        System x = systemRepository.save(system);
        log.debug(x.toString());
        return x;
    }

    /**
     * Get all the systems.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<System> findAll(Pageable pageable) {
        log.debug("Request to get all Systems");
        return systemRepository.findByStatusTrue();
    }


    /**
     * Get one system by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<System> findOne(Long id) {
        log.debug("Request to get System : {}", id);
        return systemRepository.findById(id);
    }

    /**
     * Delete the system by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete System : {}", id);        systemRepository.deleteById(id);
    }

    @Override
    public System findByStatusTrue() {
        Optional<User> userOptional = userService.getUserWithAuthorities();
        if (userOptional.isPresent()) {
            Optional<System> byStatusTrue = systemRepository.findByStatusTrue();
            if (byStatusTrue.isPresent()) {
//                for (Authority auth: userOptional.get().getAuthorities()) {
//                    if (auth.getName().equalsIgnoreCase(AuthoritiesConstants.ADMIN)) {
//                        return new System(byStatusTrue.get().getFeePercentLv1());
//                    }
//                }
//                return new System(byStatusTrue.get().getFeePercentLv2());
                return byStatusTrue.get();
            }
        }
        throw new BadRequestAlertException("Het phien lam viec", "system", "login");
    }
}
