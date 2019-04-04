package com.hieugie.banthe.web.rest;

import com.hieugie.banthe.domain.DemandDtl;
import com.hieugie.banthe.service.DemandDtlService;
import com.hieugie.banthe.web.rest.errors.BadRequestAlertException;
import com.hieugie.banthe.web.rest.util.HeaderUtil;
import com.hieugie.banthe.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing DemandDtl.
 */
@RestController
@RequestMapping("/api")
public class DemandDtlResource {

    private final Logger log = LoggerFactory.getLogger(DemandDtlResource.class);

    private static final String ENTITY_NAME = "demandDtl";

    private final DemandDtlService demandDtlService;

    public DemandDtlResource(DemandDtlService demandDtlService) {
        this.demandDtlService = demandDtlService;
    }

    /**
     * POST  /demand-dtls : Create a new demandDtl.
     *
     * @param demandDtl the demandDtl to create
     * @return the ResponseEntity with status 201 (Created) and with body the new demandDtl, or with status 400 (Bad Request) if the demandDtl has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/demand-dtls")
    public ResponseEntity<DemandDtl> createDemandDtl(@RequestBody DemandDtl demandDtl) throws URISyntaxException {
        log.debug("REST request to save DemandDtl : {}", demandDtl);
        if (demandDtl.getId() != null) {
            throw new BadRequestAlertException("A new demandDtl cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DemandDtl result = demandDtlService.save(demandDtl);
        return ResponseEntity.created(new URI("/api/demand-dtls/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /demand-dtls : Updates an existing demandDtl.
     *
     * @param demandDtl the demandDtl to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated demandDtl,
     * or with status 400 (Bad Request) if the demandDtl is not valid,
     * or with status 500 (Internal Server Error) if the demandDtl couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/demand-dtls")
    public ResponseEntity<DemandDtl> updateDemandDtl(@RequestBody DemandDtl demandDtl) throws URISyntaxException {
        log.debug("REST request to update DemandDtl : {}", demandDtl);
        if (demandDtl.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        DemandDtl result = demandDtlService.save(demandDtl);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, demandDtl.getId().toString()))
            .body(result);
    }

    /**
     * GET  /demand-dtls : get all the demandDtls.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of demandDtls in body
     */
    @GetMapping("/demand-dtls")
    public ResponseEntity<List<DemandDtl>> getAllDemandDtls(Pageable pageable) {
        log.debug("REST request to get a page of DemandDtls");
        Page<DemandDtl> page = demandDtlService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/demand-dtls");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /demand-dtls/:id : get the "id" demandDtl.
     *
     * @param id the id of the demandDtl to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the demandDtl, or with status 404 (Not Found)
     */
    @GetMapping("/demand-dtls/{id}")
    public ResponseEntity<DemandDtl> getDemandDtl(@PathVariable Long id) {
        log.debug("REST request to get DemandDtl : {}", id);
        Optional<DemandDtl> demandDtl = demandDtlService.findOne(id);
        return ResponseUtil.wrapOrNotFound(demandDtl);
    }

    /**
     * DELETE  /demand-dtls/:id : delete the "id" demandDtl.
     *
     * @param id the id of the demandDtl to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/demand-dtls/{id}")
    public ResponseEntity<Void> deleteDemandDtl(@PathVariable Long id) {
        log.debug("REST request to delete DemandDtl : {}", id);
        demandDtlService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
