package com.hieugie.banthe.web.rest;

import com.hieugie.banthe.domain.Demand;
import com.hieugie.banthe.service.DemandService;
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
 * REST controller for managing Demand.
 */
@RestController
@RequestMapping("/api")
public class DemandResource {

    private final Logger log = LoggerFactory.getLogger(DemandResource.class);

    private static final String ENTITY_NAME = "demand";

    private final DemandService demandService;

    public DemandResource(DemandService demandService) {
        this.demandService = demandService;
    }

    /**
     * POST  /demands : Create a new demand.
     *
     * @param demand the demand to create
     * @return the ResponseEntity with status 201 (Created) and with body the new demand, or with status 400 (Bad Request) if the demand has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/demands")
    public ResponseEntity<Demand> createDemand(@RequestBody Demand demand) throws URISyntaxException {
        log.debug("REST request to save Demand : {}", demand);
        if (demand.getId() != null) {
            throw new BadRequestAlertException("A new demand cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Demand result = demandService.save(demand);
        return ResponseEntity.created(new URI("/api/demands/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /demands : Updates an existing demand.
     *
     * @param demand the demand to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated demand,
     * or with status 400 (Bad Request) if the demand is not valid,
     * or with status 500 (Internal Server Error) if the demand couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/demands")
    public ResponseEntity<Demand> updateDemand(@RequestBody Demand demand) throws URISyntaxException {
        log.debug("REST request to update Demand : {}", demand);
        if (demand.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Demand result = demandService.save(demand);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, demand.getId().toString()))
            .body(result);
    }

    /**
     * GET  /demands : get all the demands.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of demands in body
     */
    @GetMapping("/demands")
    public ResponseEntity<List<Demand>> getAllDemands(Pageable pageable) {
        log.debug("REST request to get a page of Demands");
        Page<Demand> page = demandService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/demands");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /demands/:id : get the "id" demand.
     *
     * @param id the id of the demand to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the demand, or with status 404 (Not Found)
     */
    @GetMapping("/demands/{id}")
    public ResponseEntity<Demand> getDemand(@PathVariable Long id) {
        log.debug("REST request to get Demand : {}", id);
        Optional<Demand> demand = demandService.findOne(id);
        return ResponseUtil.wrapOrNotFound(demand);
    }

    /**
     * DELETE  /demands/:id : delete the "id" demand.
     *
     * @param id the id of the demand to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/demands/{id}")
    public ResponseEntity<Void> deleteDemand(@PathVariable Long id) {
        log.debug("REST request to delete Demand : {}", id);
        demandService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
