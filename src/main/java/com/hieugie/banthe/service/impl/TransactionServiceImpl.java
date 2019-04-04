package com.hieugie.banthe.service.impl;

import com.google.common.base.Strings;
import com.hieugie.banthe.domain.Transaction;
import com.hieugie.banthe.domain.User;
import com.hieugie.banthe.repository.OtpRepository;
import com.hieugie.banthe.repository.TransactionRepository;
import com.hieugie.banthe.repository.UserRepository;
import com.hieugie.banthe.service.TransactionService;
import com.hieugie.banthe.service.UserService;
import com.hieugie.banthe.service.util.Common;
import com.hieugie.banthe.service.util.GoogleAuthenticaionUtils;
import com.hieugie.banthe.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing Transaction.
 */
@Service
@Transactional
public class TransactionServiceImpl implements TransactionService {

    private final Logger log = LoggerFactory.getLogger(TransactionServiceImpl.class);

    private final TransactionRepository transactionRepository;

    private UserService userService;

    private OtpRepository otpRepository;

    private UserRepository userRepository;

    public TransactionServiceImpl(TransactionRepository transactionRepository, UserService userService, OtpRepository otpRepository, UserRepository userRepository) {
        this.transactionRepository = transactionRepository;
        this.userService = userService;
        this.otpRepository = otpRepository;
        this.userRepository = userRepository;
    }

    /**
     * Save a transaction.
     *
     * @param transaction the entity to save
     * @return the persisted entity
     */
    @Override
    public Transaction save(Transaction transaction) {
        log.debug("Request to save Transaction : {}", transaction);
        // Kiểm tra mã otp
        Optional<User> userOptional =  userService.getUserWithAuthorities();
        if (userOptional.isPresent()) {

            if (transaction.getAmount().longValue() % 1000 != 0) {
                throw new BadRequestAlertException("Ma otp khong chinh xac", "transaction", "invalidAmount");
            }
            User user = userOptional.get();
            if (Strings.isNullOrEmpty(user.getPrivateKey())) {
                throw new BadRequestAlertException("Ma otp khong chinh xac", "transaction", "noneOtp");
            }
            boolean check = GoogleAuthenticaionUtils.performAuthentication(transaction.getOtp(), user.getPrivateKey(), user.getLastVerifiedTime(), user.getLastVerifiedCode());
            if (!check) {
                throw new BadRequestAlertException("Ma otp khong chinh xac", "transaction", "invalidOtp");
            }

            // nếu là admin thì vô hạn chuyển
            // Kiểm tra số tiền hiện của của đại lý, và số tiền muốn chuyển sang đại lý con
            if (!Common.isAdmin(user) && user.getAmount().compareTo(transaction.getAmount()) < 0) {
                throw new BadRequestAlertException("Khong du tien de chuyen", "transaction", "invalidAmount");
            }
            transaction.setFromUser(user);
            transaction.setStatus(1);
            user.setAmount(user.getAmount() != null ? user.getAmount().add(transaction.getAmount()) : transaction.getAmount());
            user.setAvailableAmount(user.getAvailableAmount() != null ? user.getAvailableAmount().add(transaction.getAmount()) : transaction.getAmount());

            User toUser = transaction.getToUser();
            toUser.setAmount(toUser.getAmount() != null ? toUser.getAmount().add(transaction.getAmount()) : transaction.getAmount());
            toUser.setAvailableAmount(toUser.getAvailableAmount() != null ? toUser.getAvailableAmount().add(transaction.getAmount()) : transaction.getAmount());

            userRepository.updateAmount(toUser.getAmount().longValue(), toUser.getAvailableAmount().longValue(), toUser.getId());
            if (!Common.isAdmin(user)) {
                userRepository.updateAmount(user.getAmount().longValue(), toUser.getAvailableAmount().longValue(), user.getId());
            }

            userService.clearUserCaches(toUser);
            userService.clearUserCaches(user);

            return transactionRepository.save(transaction);
        }
        throw new BadRequestAlertException("User het hạn dang nhap", "transaction", "invalidUser");
    }

    /**
     * Get all the transactions.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Transaction> findAll(Pageable pageable) {
        log.debug("Request to get all Transactions");
        Optional<User> userOptional =  userService.getUserWithAuthorities();
        if (userOptional.isPresent()) {
            return transactionRepository.findByFromUserIdAndStatusNot(pageable, userOptional.get().getId(), 3);
        }
        return Page.empty();
    }


    /**
     * Get one transaction by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Transaction> findOne(Long id) {
        log.debug("Request to get Transaction : {}", id);
        return transactionRepository.findById(id);
    }

    /**
     * Delete the transaction by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Transaction : {}", id);        transactionRepository.deleteById(id);
    }
}
