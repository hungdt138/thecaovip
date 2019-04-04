package com.hieugie.banthe.web.rest;

import com.hieugie.banthe.domain.DemandCharge;
import com.hieugie.banthe.service.DemandChargeService;
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
 * REST controller for managing DemandCharge.
 */
@RestController
@RequestMapping("/api")
public class DemandChargeResource {

    private final Logger log = LoggerFactory.getLogger(DemandChargeResource.class);

    private static final String ENTITY_NAME = "demandCharge";

    private final DemandChargeService demandChargeService;

    public DemandChargeResource(DemandChargeService demandChargeService) {
        this.demandChargeService = demandChargeService;
    }

    /**
     * POST  /demand-charges : Create a new demandCharge.
     *
     * @param demandCharge the demandCharge to create
     * @return the ResponseEntity with status 201 (Created) and with body the new demandCharge, or with status 400 (Bad Request) if the demandCharge has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/demand-charges")
    public ResponseEntity<DemandCharge> createDemandCharge(@RequestBody DemandCharge demandCharge) throws URISyntaxException {
        log.debug("REST request to save DemandCharge : {}", demandCharge);
        if (demandCharge.getId() != null) {
            throw new BadRequestAlertException("A new demandCharge cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DemandCharge result = demandChargeService.save(demandCharge);
        return ResponseEntity.created(new URI("/api/demand-charges/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /demand-charges : Updates an existing demandCharge.
     *
     * @param demandCharge the demandCharge to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated demandCharge,
     * or with status 400 (Bad Request) if the demandCharge is not valid,
     * or with status 500 (Internal Server Error) if the demandCharge couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/demand-charges")
    public ResponseEntity<DemandCharge> updateDemandCharge(@RequestBody DemandCharge demandCharge) throws URISyntaxException {
        log.debug("REST request to update DemandCharge : {}", demandCharge);
        if (demandCharge.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        DemandCharge result = demandChargeService.save(demandCharge);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, demandCharge.getId().toString()))
            .body(result);
    }

    /**
     * GET  /demand-charges : get all the demandCharges.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of demandCharges in body
     */
    @GetMapping("/demand-charges")
    public ResponseEntity<List<DemandCharge>> getAllDemandCharges(Pageable pageable) {
        log.debug("REST request to get a page of DemandCharges");
        Page<DemandCharge> page = demandChargeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/demand-charges");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/demand-charges/hand")
    public ResponseEntity<List<DemandCharge>> getAllDemandChargesHand(Pageable pageable) {
        log.debug("REST request to get a page of DemandCharges");
        Page<DemandCharge> page = demandChargeService.findAllHand(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/demand-charges");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /demand-charges/:id : get the "id" demandCharge.
     *
     * @param id the id of the demandCharge to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the demandCharge, or with status 404 (Not Found)
     */
    @GetMapping("/demand-charges/{id}")
    public ResponseEntity<DemandCharge> getDemandCharge(@PathVariable Long id) {
        log.debug("REST request to get DemandCharge : {}", id);
        Optional<DemandCharge> demandCharge = demandChargeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(demandCharge);
    }

    /**
     * DELETE  /demand-charges/:id : delete the "id" demandCharge.
     *
     * @param id the id of the demandCharge to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/demand-charges/{id}")
    public ResponseEntity<Void> deleteDemandCharge(@PathVariable Long id) {
        log.debug("REST request to delete DemandCharge : {}", id);
        demandChargeService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    @GetMapping("/demand-charges/search")
    public ResponseEntity<List<DemandCharge>> search(Pageable pageable, @RequestParam(required = false) String code,
                                                     @RequestParam(required = false) String seri) {
        log.debug("REST request to get a page of DemandCharges");
        Page<DemandCharge> page = demandChargeService.search(pageable, code, seri);
        String s = page.getContent().get(0).getMessage();
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/demand-charges");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
