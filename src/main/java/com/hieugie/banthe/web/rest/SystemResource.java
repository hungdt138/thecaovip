package com.hieugie.banthe.web.rest;

import com.hieugie.banthe.domain.System;
import com.hieugie.banthe.security.AuthoritiesConstants;
import com.hieugie.banthe.service.SystemService;
import com.hieugie.banthe.web.rest.errors.BadRequestAlertException;
import com.hieugie.banthe.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

/**
 * REST controller for managing System.
 */
@RestController
@RequestMapping("/api")
public class SystemResource {

    private final Logger log = LoggerFactory.getLogger(SystemResource.class);

    private static final String ENTITY_NAME = "system";

    private final SystemService systemService;

    public SystemResource(SystemService systemService) {
        this.systemService = systemService;
    }

    /**
     * POST  /systems : Create a new system.
     *
     * @param system the system to create
     * @return the ResponseEntity with status 201 (Created) and with body the new system, or with status 400 (Bad Request) if the system has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/systems")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<System> createSystem(@Valid @RequestBody System system) throws URISyntaxException {
        log.debug("REST request to save System : {}", system);
        if (system.getId() != null) {
            throw new BadRequestAlertException("A new system cannot already have an ID", ENTITY_NAME, "idexists");
        }
        System result = systemService.save(system);
        return ResponseEntity.created(new URI("/api/systems/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /systems : Updates an existing system.
     *
     * @param system the system to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated system,
     * or with status 400 (Bad Request) if the system is not valid,
     * or with status 500 (Internal Server Error) if the system couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/systems")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<System> updateSystem(@Valid @RequestBody System system) throws URISyntaxException {
        log.debug("REST request to update System : {}", system);
        if (system.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        System result = systemService.save(system);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, system.getId().toString()))
            .body(result);
    }

    /**
     * GET  /systems : get all the systems.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of systems in body
     */
    @GetMapping("/systems")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<System> getAllSystems(Pageable pageable) {
        log.debug("REST request to get a page of Systems");
        Optional<System> system =  systemService.findAll(pageable);
        return new ResponseEntity<>(system.orElse(new System()), HttpStatus.OK);
    }

    /**
     * GET  /systems/:id : get the "id" system.
     *
     * @param id the id of the system to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the system, or with status 404 (Not Found)
     */
    @GetMapping("/systems/{id}")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<System> getSystem(@PathVariable Long id) {
        log.debug("REST request to get System : {}", id);
        Optional<System> system = systemService.findOne(id);
        return ResponseUtil.wrapOrNotFound(system);
    }

    @GetMapping("/systems/get-one")
    public ResponseEntity<System> getOneSystem() {
        log.debug("REST request to get System ");
        System system = systemService.findByStatusTrue();
        return new ResponseEntity<>(system, HttpStatus.OK);
    }

    /**
     * DELETE  /systems/:id : delete the "id" system.
     *
     * @param id the id of the system to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/systems/{id}")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Void> deleteSystem(@PathVariable Long id) {
        log.debug("REST request to delete System : {}", id);
        systemService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
