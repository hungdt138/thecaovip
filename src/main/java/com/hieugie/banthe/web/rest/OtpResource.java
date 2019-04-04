package com.hieugie.banthe.web.rest;

import com.hieugie.banthe.domain.Otp;
import com.hieugie.banthe.service.OtpService;
import io.micrometer.core.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URISyntaxException;

/**
 * REST controller for managing Otp.
 */
@RestController
@RequestMapping("/api")
public class OtpResource {

    private final Logger log = LoggerFactory.getLogger(OtpResource.class);

    private static final String ENTITY_NAME = "otp";

    private final OtpService otpService;

    public OtpResource(OtpService otpService) {
        this.otpService = otpService;
    }

    /**
     * @Author hieugie
     *
     * Send otp đến 1 số điện thoại
     *
     * @param otp
     * @return
     * @throws URISyntaxException
     */
    @PostMapping("/p/otp/send")
    @Timed
    public ResponseEntity<Void> sendOtp(@RequestBody Otp otp) {
        log.debug("REST request to save Otp : {}", otp);
        otpService.save(otp);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
