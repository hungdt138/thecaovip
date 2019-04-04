package com.hieugie.banthe.web.rest;

import com.hieugie.banthe.domain.Transaction;
import com.hieugie.banthe.service.ReportService;
import com.hieugie.banthe.service.TransactionService;
import com.hieugie.banthe.web.rest.dto.ReportDTO;
import com.hieugie.banthe.web.rest.errors.BadRequestAlertException;
import com.hieugie.banthe.web.rest.util.HeaderUtil;
import com.hieugie.banthe.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Transaction.
 */
@RestController
@RequestMapping("/api")
public class ReportResource {

    private final Logger log = LoggerFactory.getLogger(ReportResource.class);

    private static final String ENTITY_NAME = "report";

    private final ReportService reportService;

    public ReportResource(ReportService reportService) {
        this.reportService = reportService;
    }

    /**
     * GET  /reports : get reports.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of transactions in body
     */
    @GetMapping("/reports")
    public ResponseEntity<ReportDTO> getReport(String user, String fromDate, String toDate) {
        log.debug("REST request to get a page of Transactions");
        ReportDTO report = reportService.getReport(user, fromDate, toDate);
        return new ResponseEntity<>(report, HttpStatus.OK);
    }

}
