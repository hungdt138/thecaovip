package com.hieugie.banthe.service.impl;

import com.hieugie.banthe.domain.Otp;
import com.hieugie.banthe.domain.User;
import com.hieugie.banthe.repository.OtpRepository;
import com.hieugie.banthe.service.OtpService;
import com.hieugie.banthe.service.UserService;
import com.hieugie.banthe.service.util.Authentication;
import com.hieugie.banthe.service.util.SMSUtils;
import com.hieugie.banthe.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Service Implementation for managing Otp.
 */
@Service
@Transactional
public class OtpServiceImpl implements OtpService {

    private final Logger log = LoggerFactory.getLogger(OtpServiceImpl.class);

    private final OtpRepository otpRepository;

    private UserService userService;

    public OtpServiceImpl(OtpRepository otpRepository, UserService userService) {
        this.otpRepository = otpRepository;
        this.userService = userService;
    }

    /**
     * Save a otp.
     *
     * @param otp the entity to save
     * @return the persisted entity
     */
    @Override
    public Otp save(Otp otp) {
        log.debug("Request to save Otp : {}", otp);


        Optional<User> user = userService.getUserWithAuthorities();

        if (user.isPresent()){
            // Trường hợp gửi lại otp, update mã otp cũ,
            // và tạo 1 mã otp mới
            if (otp.getResend() != null && otp.getResend()) {
                otp.setId(null);
            }
            otpRepository.updateAllOtpByActionAndPhoneNumber(otp.getAction().getValue(), user.get().getPhoneNumber());
            otp.setStatus(true);
            otp.setExpiredDate(LocalDateTime.now().plusHours(7).plusMinutes(1));
            otp.setCode(Authentication.GoogleAuthenticatorCode(SMSUtils.SECRET_CODE));
            otp.setPhoneNumber(user.get().getPhoneNumber());
            boolean isSend = SMSUtils.sendOtp(user.get().getPhoneNumber(), otp.getCode());
//            if (isSend) {
                return otpRepository.save(otp);
//            }
        }

        throw new BadRequestAlertException("SO DIEN THOAI KHONG TON TAI", "OTP", "phoneNumber");
    }

    /**
     * Get all the otps.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Otp> findAll(Pageable pageable) {
        log.debug("Request to get all Otps");
        return otpRepository.findAll(pageable);
    }


    /**
     * Get one otp by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Otp> findOne(Long id) {
        log.debug("Request to get Otp : {}", id);
        return otpRepository.findById(id);
    }

    /**
     * Delete the otp by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Otp : {}", id);        otpRepository.deleteById(id);
    }
}
