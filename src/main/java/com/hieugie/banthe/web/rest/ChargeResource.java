package com.hieugie.banthe.web.rest;

import com.hieugie.banthe.domain.DemandCharge;
import com.hieugie.banthe.service.ChargeService;
import com.hieugie.banthe.web.rest.dto.CallBackResponse;
import com.hieugie.banthe.web.rest.dto.ChargeRequestDTO;
import com.hieugie.banthe.web.rest.dto.ChargeResponseDTO;
import com.hieugie.banthe.web.rest.dto.DemandChargeDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URISyntaxException;

@RestController
@RequestMapping("/api")
public class ChargeResource {

    private final Logger log = LoggerFactory.getLogger(ChargeResource.class);

    private ChargeService chargeService;

    public ChargeResource(ChargeService chargeService) {
        this.chargeService = chargeService;
    }

    @PostMapping("/p/charge")
    public ResponseEntity<ChargeResponseDTO> charge(@Valid @RequestBody ChargeRequestDTO chargeRequestDTO) throws URISyntaxException {
        log.debug("REST request to save Charge : {}", chargeRequestDTO);
        ChargeResponseDTO result = chargeService.charge(chargeRequestDTO);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/confirm")
    public ResponseEntity<CallBackResponse> confirm(@RequestBody DemandChargeDTO demandCharge) throws URISyntaxException {
        log.debug("REST request to save Charge : {}", demandCharge);
        CallBackResponse result = chargeService.confirm(demandCharge);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
