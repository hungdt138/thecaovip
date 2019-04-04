package com.hieugie.banthe.web.rest;

import com.hieugie.banthe.domain.Bill;
import com.hieugie.banthe.service.BillService;
import com.hieugie.banthe.web.rest.dto.BillDTO;
import com.hieugie.banthe.web.rest.dto.MyBillDTO;
import com.hieugie.banthe.web.rest.dto.SheetDTO;
import com.hieugie.banthe.web.rest.errors.BadRequestAlertException;
import com.hieugie.banthe.web.rest.util.HeaderUtil;
import com.hieugie.banthe.web.rest.util.PaginationUtil;
import io.micrometer.core.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

/**
 * REST controller for managing Bill.
 */
@RestController
@RequestMapping("/api")
public class BillResource {

    private final Logger log = LoggerFactory.getLogger(BillResource.class);

    private static final String ENTITY_NAME = "bill";

    private final BillService billService;

    public BillResource(BillService billService) {
        this.billService = billService;
    }

    /**
     * POST  /bills : Create a new bill.
     *
     * @param bill the bill to create
     * @return the ResponseEntity with status 201 (Created) and with body the new bill, or with status 400 (Bad Request) if the bill has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/bills")
    public ResponseEntity<Bill> createBill(@Valid @RequestBody BillDTO bill) throws URISyntaxException {
        log.debug("REST request to save Bill : {}", bill);
        if (bill.getBill().getId() != null) {
            throw new BadRequestAlertException("A new bill cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Bill result = billService.save(bill);
        return ResponseEntity.created(new URI("/api/bills/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /bills : Updates an existing bill.
     *
     * @param bill the bill to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated bill,
     * or with status 400 (Bad Request) if the bill is not valid,
     * or with status 500 (Internal Server Error) if the bill couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/bills")
    public ResponseEntity<Bill> updateBill(@Valid @RequestBody BillDTO bill) throws URISyntaxException {
        log.debug("REST request to update Bill : {}", bill);
        if (bill.getBill().getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Bill result = billService.save(bill);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, bill.getBill().getId().toString()))
            .body(result);
    }

    /**
     * GET  /bills : get all the bills.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of bills in body
     */
    @GetMapping("/bills")
    public ResponseEntity<List<MyBillDTO>> getAllBills(Pageable pageable,
                                                       @RequestParam(required = false) String account,
                                                       @RequestParam(required = false) String partnerId,
                                                       @RequestParam(required = false) String fullName,
                                                       @RequestParam(required = false) Integer status) {
        log.debug("REST request to get a page of Bills");
        Page<MyBillDTO> page = billService.findAll(pageable, account, partnerId, fullName, status);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/bills");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /bills/:id : get the "id" bill.
     *
     * @param id the id of the bill to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the bill, or with status 404 (Not Found)
     */
    @GetMapping("/bills/{id}")
    public ResponseEntity<MyBillDTO> getBill(@PathVariable Long id) {
        log.debug("REST request to get Bill : {}", id);
        MyBillDTO bill = billService.findOne(id);
        return new ResponseEntity<>(bill, HttpStatus.OK);
    }

    @GetMapping("/bills/charge/{id}")
    public ResponseEntity<BillDTO> getBill2(@PathVariable Long id) {
        log.debug("REST request to get Bill : {}", id);
        BillDTO bill = billService.findById2(id);
        return new ResponseEntity<>(bill, HttpStatus.OK);
    }

    /**
     * DELETE  /bills/:id : delete the "id" bill.
     *
     * @param id the id of the bill to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/bills/{id}")
    public ResponseEntity<Void> deleteBill(@PathVariable Long id) {
        log.debug("REST request to delete Bill : {}", id);
        billService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    @PostMapping(value = "/bills/read-sheet-name", produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<SheetDTO>> readSheetName(@RequestParam(required = false) MultipartFile file) throws URISyntaxException, IOException {
        log.debug("REST request to save CardPack : {}", file.getOriginalFilename());
        List<SheetDTO> result = billService.readSheetName(file);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping(value = "/bills/read-sheet", produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<BillDTO> readSheet(@RequestParam(required = false) MultipartFile file,
                                             @RequestParam(required = false) Integer sheetNumber) throws URISyntaxException, IOException {
        log.debug("REST request to save CardPack : {}", file.getOriginalFilename());
        BillDTO result = billService.readSheet(file, sheetNumber);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
