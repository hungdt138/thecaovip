package com.hieugie.banthe.service;

import com.google.common.base.Strings;
import com.hieugie.banthe.config.Constants;
import com.hieugie.banthe.domain.Authority;
import com.hieugie.banthe.domain.System;
import com.hieugie.banthe.domain.User;
import com.hieugie.banthe.repository.AuthorityRepository;
import com.hieugie.banthe.repository.SystemRepository;
import com.hieugie.banthe.repository.TransactionRepository;
import com.hieugie.banthe.repository.UserRepository;
import com.hieugie.banthe.security.AuthoritiesConstants;
import com.hieugie.banthe.security.SecurityUtils;
import com.hieugie.banthe.service.dto.UserDTO;
import com.hieugie.banthe.service.util.GoogleAuthenticaionUtils;
import com.hieugie.banthe.service.util.RandomUtil;
import com.hieugie.banthe.web.rest.dto.TwoFa;
import com.hieugie.banthe.web.rest.errors.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service class for managing users.
 */
@Service
@Transactional
public class UserService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthorityRepository authorityRepository;

    private final CacheManager cacheManager;

    private final SystemRepository systemRepository;

    private final TransactionRepository transactionRepository;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthorityRepository authorityRepository, CacheManager cacheManager, SystemRepository systemRepository, TransactionRepository transactionRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authorityRepository = authorityRepository;
        this.cacheManager = cacheManager;
        this.systemRepository = systemRepository;
        this.transactionRepository = transactionRepository;
    }

    public Optional<User> activateRegistration(String key) {
        log.debug("Activating user for activation key {}", key);
        return userRepository.findOneByActivationKey(key)
            .map(user -> {
                // activate given user for the registration key.
                user.setActivated(true);
                user.setActivationKey(null);
                this.clearUserCaches(user);
                log.debug("Activated user: {}", user);
                return user;
            });
    }

    public Optional<User> completePasswordReset(String newPassword, String key) {
        log.debug("Reset user password for reset key {}", key);
        return userRepository.findOneByResetKey(key)
            .filter(user -> user.getResetDate().isAfter(Instant.now().minusSeconds(86400)))
            .map(user -> {
                user.setPassword(passwordEncoder.encode(newPassword));
                user.setResetKey(null);
                user.setResetDate(null);
                this.clearUserCaches(user);
                return user;
            });
    }

    public Optional<User> requestPasswordReset(String mail) {
        return userRepository.findOneByEmailIgnoreCase(mail)
            .filter(User::getActivated)
            .map(user -> {
                user.setResetKey(RandomUtil.generateResetKey());
                user.setResetDate(Instant.now());
                this.clearUserCaches(user);
                return user;
            });
    }

    public User registerUser(UserDTO userDTO, String password) {
        userRepository.findOneByLogin(userDTO.getLogin().toLowerCase()).ifPresent(existingUser -> {
            boolean removed = removeNonActivatedUser(existingUser);
            if (!removed) {
                throw new LoginAlreadyUsedException();
            }
        });
        userRepository.findOneByEmailIgnoreCase(userDTO.getEmail()).ifPresent(existingUser -> {
            boolean removed = removeNonActivatedUser(existingUser);
            if (!removed) {
                throw new EmailAlreadyUsedException();
            }
        });
        User newUser = new User();
        String encryptedPassword = passwordEncoder.encode(password);
        newUser.setLogin(userDTO.getLogin().toLowerCase());
        // new user gets initially a generated password
        newUser.setPassword(encryptedPassword);
        newUser.setFullName(userDTO.getFullName());
        newUser.setEmail(userDTO.getEmail().toLowerCase());
        newUser.setImageUrl(userDTO.getImageUrl());
        newUser.setLangKey(userDTO.getLangKey());
        // new user is not active
        newUser.setActivated(false);
        // new user gets registration key
        newUser.setActivationKey(RandomUtil.generateActivationKey());
        Set<Authority> authorities = new HashSet<>();
        authorityRepository.findById(AuthoritiesConstants.USER).ifPresent(authorities::add);
        newUser.setAuthorities(authorities);
        userRepository.save(newUser);
        this.clearUserCaches(newUser);
        log.debug("Created Information for User: {}", newUser);
        return newUser;
    }

    private boolean removeNonActivatedUser(User existingUser) {
        if (existingUser.getActivated()) {
            return false;
        }
        userRepository.delete(existingUser);
        userRepository.flush();
        this.clearUserCaches(existingUser);
        return true;
    }

    public User createUser(UserDTO userDTO) {
        User user = new User();
        user.setLogin(userDTO.getLogin().toLowerCase());
        user.setFullName(userDTO.getFullName());
        user.setEmail(userDTO.getEmail().toLowerCase());
        user.setImageUrl(userDTO.getImageUrl());
        if (userDTO.getLangKey() == null) {
            user.setLangKey(Constants.DEFAULT_LANGUAGE); // default language
        } else {
            user.setLangKey(userDTO.getLangKey());
        }
        String encryptedPassword = passwordEncoder.encode(RandomUtil.generatePassword());
        user.setPassword(encryptedPassword);
        user.setResetKey(RandomUtil.generateResetKey());
        user.setResetDate(Instant.now());
        user.setActivated(true);
        if (userDTO.getAuthorities() != null) {
            Set<Authority> authorities = userDTO.getAuthorities().stream()
                .map(authorityRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());
            user.setAuthorities(authorities);
        }
        userRepository.save(user);
        this.clearUserCaches(user);
        log.debug("Created Information for User: {}", user);
        return user;
    }

    public User createBranchUser(UserDTO userDTO, String password) {
        // Kiểm tra chiết khấu của user
        // chiết khẩu phải lớn hơn hoặc bằng của % đại lý cấp 1, và nhỏ hơn 100%

        Optional<User> userOptional = getUserWithAuthorities();
        if (!userOptional.isPresent()) {
            throw new BadRequestAlertException("Het phien lam viec", "userservice", "login");
        }
        checkTrietKhau(userDTO, userOptional.get());

        User user = new User();
        user.setLogin(userDTO.getLogin().toLowerCase());
        user.setFullName(userDTO.getFullName().trim());
        user.setEmail(!Strings.isNullOrEmpty(userDTO.getEmail()) ? userDTO.getEmail().toLowerCase() : null);
        if (userDTO.getLangKey() == null) {
            user.setLangKey(Constants.DEFAULT_LANGUAGE); // default language
        } else {
            user.setLangKey(userDTO.getLangKey());
        }
        user.setResetKey(RandomUtil.generateResetKey());
        String encryptedPassword = passwordEncoder.encode(password);
        user.setUserId(userOptional.get().getId());
        user.setPassword(encryptedPassword);
        user.setResetDate(Instant.now());
        user.setActivated(true);
        user.setPhoneNumber(userDTO.getPhoneNumber());
        user.setFeePercentLv1(userDTO.getFeePercentLv1());
        user.setFeePercentLv2(userDTO.getFeePercentLv2());
        user.setFeePercentLv1b(userDTO.getFeePercentLv1b());
        user.setFeePercentLv2b(userDTO.getFeePercentLv2b());
        user.setAvailableAmount(BigDecimal.ZERO);
        user.setAmount(BigDecimal.ZERO);

        Set<Authority> authorities = new HashSet<>();
        authorityRepository.findById(AuthoritiesConstants.USER).ifPresent(authorities::add);
        user.setAuthorities(authorities);
        userRepository.save(user);
        this.clearUserCaches(user);
        log.debug("Created Information for User: {}", user);
        return user;
    }

    /**
     * Update basic information (first name, last name, email, language) for the current user.
     *
     * @param fullName full name of user
     */
    public void updateUser(String fullName, String phoneNumber) {
        SecurityUtils.getCurrentUserLogin()
            .flatMap(userRepository::findOneByLogin)
            .ifPresent(user -> {
                user.setFullName(fullName);
                user.setPhoneNumber(phoneNumber);
                this.clearUserCaches(user);
                log.debug("Changed Information for User: {}", user);
            });
    }

    /**
     * Update all information for a specific user, and return the modified user.
     *
     * @param userDTO user to update
     * @return updated user
     */
    public Optional<UserDTO> updateUser(UserDTO userDTO) {
        return Optional.of(userRepository
            .findById(userDTO.getId()))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .map(user -> {
                this.clearUserCaches(user);
                user.setLogin(userDTO.getLogin().toLowerCase());
                user.setFullName(userDTO.getFullName());
                user.setImageUrl(userDTO.getImageUrl());
                user.setActivated(userDTO.isActivated());
                user.setLangKey(userDTO.getLangKey());
//                user.setEmail(userDTO.getEmail().toLowerCase());
                Set<Authority> managedAuthorities = user.getAuthorities();
                managedAuthorities.clear();
                userDTO.getAuthorities().stream()
                    .map(authorityRepository::findById)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .forEach(managedAuthorities::add);
                this.clearUserCaches(user);
                log.debug("Changed Information for User: {}", user);
                return user;
            })
            .map(UserDTO::new);
    }

    public Optional<UserDTO> updateBranchUser(UserDTO userDTO) {
        // để update được thông tin user đại lý, user đó phải là đại lý của user đang đăng nhập
        Optional<User> userWithAuthorities = this.getUserWithAuthorities();

        if (userWithAuthorities.isPresent()) {
            checkTrietKhau(userDTO, userWithAuthorities.get());
            Optional<User> updateUser = userRepository.findById(userDTO.getId());
            if (!updateUser.isPresent()) {
                throw new BadRequestAlertException("Tai khoan cua dai ly khong ton tai", "create-account", "userNotFound");
            } else if (updateUser.get().getUserId().equals(userWithAuthorities.get().getId())) {
                User user = updateUser.get();
                user.setLogin(userDTO.getLogin().toLowerCase());
                user.setFullName(userDTO.getFullName());
                user.setPhoneNumber(userDTO.getPhoneNumber());
                user.setFeePercentLv1(userDTO.getFeePercentLv1());
                user.setFeePercentLv2(userDTO.getFeePercentLv2());
                user.setFeePercentLv1b(userDTO.getFeePercentLv1b());
                user.setFeePercentLv2b(userDTO.getFeePercentLv2b());

                userRepository.save(user);
                this.clearUserCaches(user);
                return Optional.of(user).map(UserDTO::new);
            } else {
                throw new BadRequestAlertException("Dai ly khong thuoc quyen quan ly cua ban", "create-account", "userNotBelong");
            }
        }
        throw new BadRequestAlertException("Phien lam viec da het han, hay dang nhap lai", "create-account", "logout");
    }

    private void checkTrietKhau(UserDTO userDTO, User user) {
        if (user.getUserId() != null) {
            User user2 = userRepository.getOne(user.getId());
            if (userDTO.getFeePercentLv1() < user2.getFeePercentLv1()) {
                throw new BadRequestAlertException(user2.getFeePercentLv1() + "", "userservice", "invalidFeePercent1");
            } else if (userDTO.getFeePercentLv2() < user2.getFeePercentLv2()) {
                throw new BadRequestAlertException(user2.getFeePercentLv2() + "", "userservice", "invalidFeePercent2");
            } else if (userDTO.getFeePercentLv1b() < user2.getFeePercentLv1b()) {
                throw new BadRequestAlertException(user2.getFeePercentLv1b() + "", "userservice", "invalidFeePercent1b");
            } else if (userDTO.getFeePercentLv2b() < user2.getFeePercentLv2b()) {
                throw new BadRequestAlertException(user2.getFeePercentLv2b() + "", "userservice", "invalidFeePercent2b");
            }
        } else {
            Optional<System> byStatusTrue = systemRepository.findByStatusTrue();
            if (!byStatusTrue.isPresent()) {
                throw new BadRequestAlertException("Co loi he thong", "userservice", "invalidSystem");
            }
            if (userDTO.getFeePercentLv1() < byStatusTrue.get().getFeePercentLv1()) {
                throw new BadRequestAlertException(byStatusTrue.get().getFeePercentLv1() + "", "userservice", "invalidFeePercent1");
            } else if (userDTO.getFeePercentLv2() < byStatusTrue.get().getFeePercentLv2()) {
                throw new BadRequestAlertException(byStatusTrue.get().getFeePercentLv2() + "", "userservice", "invalidFeePercent2");
            } else if (userDTO.getFeePercentLv1b() < byStatusTrue.get().getFeePercentLv1b()) {
                throw new BadRequestAlertException(byStatusTrue.get().getFeePercentLv1b() + "", "userservice", "invalidFeePercent1b");
            } else if (userDTO.getFeePercentLv2b() < byStatusTrue.get().getFeePercentLv2b()) {
                throw new BadRequestAlertException(byStatusTrue.get().getFeePercentLv2b() + "", "userservice", "invalidFeePercent2b");
            }
        }
    }

    public void deleteUser(String login) {
        userRepository.findOneByLogin(login).ifPresent(user -> {
            userRepository.delete(user);
            this.clearUserCaches(user);
            log.debug("Deleted User: {}", user);
        });
    }

    public void changePassword(String currentClearTextPassword, String newPassword) {
        SecurityUtils.getCurrentUserLogin()
            .flatMap(userRepository::findOneByLogin)
            .ifPresent(user -> {
                String currentEncryptedPassword = user.getPassword();
                if (!passwordEncoder.matches(currentClearTextPassword, currentEncryptedPassword)) {
                    throw new InvalidPasswordException();
                }
                String encryptedPassword = passwordEncoder.encode(newPassword);
                user.setPassword(encryptedPassword);
                this.clearUserCaches(user);
                log.debug("Changed password for User: {}", user);
            });
    }

    @Transactional(readOnly = true)
    public Page<UserDTO> getAllManagedUsers(Pageable pageable) {
        return userRepository.findAllByLoginNot(pageable, Constants.ANONYMOUS_USER).map(UserDTO::new);
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserWithAuthoritiesByLogin(String login) {
        return userRepository.findOneWithAuthoritiesByLogin(login);
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserWithAuthorities(Long id) {
        return userRepository.findOneWithAuthoritiesById(id);
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserWithAuthorities() {
        Optional<User> userOptional = SecurityUtils.getCurrentUserLogin().flatMap(userRepository::findOneWithAuthoritiesByLogin);
        if (userOptional.isPresent()) {
            userOptional.get().setAmount(transactionRepository.findAmount(userOptional.get().getId()));
            userOptional.get().setAvailableAmount(transactionRepository.findAvailableAmount(userOptional.get().getId()));
            userOptional.get().setChargeAmount(transactionRepository.getChargeAmount(userOptional.get().getId()));
            return userOptional;
        }
        return Optional.empty();
    }

    /**
     * Not activated users should be automatically deleted after 3 days.
     * <p>
     * This is scheduled to get fired everyday, at 01:00 (am).
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void removeNotActivatedUsers() {
        userRepository
            .findAllByActivatedIsFalseAndCreatedDateBefore(Instant.now().minus(3, ChronoUnit.DAYS))
            .forEach(user -> {
                log.debug("Deleting not activated user {}", user.getLogin());
                userRepository.delete(user);
                this.clearUserCaches(user);
            });
    }

    /**
     * @return a list of all the authorities
     */
    public List<String> getAuthorities() {
        return authorityRepository.findAll().stream().map(Authority::getName).collect(Collectors.toList());
    }

    public void clearUserCaches(User user) {
        Objects.requireNonNull(cacheManager.getCache(UserRepository.USERS_BY_LOGIN_CACHE)).evict(user.getLogin());
        if (!Strings.isNullOrEmpty(user.getEmail())) {
            Objects.requireNonNull(cacheManager.getCache(UserRepository.USERS_BY_EMAIL_CACHE)).evict(user.getEmail());
        }
    }

    public Page<UserDTO> getBranchuser(Pageable pageable) {
        Optional<User> userWithAuthorities = this.getUserWithAuthorities();

        Page<User> byUserId = userRepository.findByUserId(pageable, userWithAuthorities.get().getId());
        for (User user: byUserId) {
            user.setAmount(transactionRepository.findAmount(user.getId()));
            user.setAvailableAmount(transactionRepository.findAvailableAmount(user.getId()));
            user.setChargeAmount(transactionRepository.getChargeAmount(user.getId()));
        }
        return byUserId.map(UserDTO::new);
    }

    public BigDecimal getAvailableAmount() {
        Optional<User> userWithAuthorities = this.getUserWithAuthorities();
        if (userWithAuthorities.isPresent()) {
            for (Authority auth: userWithAuthorities.get().getAuthorities()) {
                if (auth.getName().equalsIgnoreCase(AuthoritiesConstants.ADMIN)) {
                    return null;
                }
            }
            return transactionRepository.findAvailableAmount(userWithAuthorities.get().getId());
        }
        return BigDecimal.ZERO;
    }

    public TwoFa get2fa() {
        Optional<User> userWithAuthorities = getUserWithAuthorities();
        if (!userWithAuthorities.isPresent()) {
            throw new BadRequestAlertException("Phien lam viec da het han, hay dang nhap lai", "create-account", "logout");
        }
        String key = GoogleAuthenticaionUtils.createRandomKey();
        String uri = GoogleAuthenticaionUtils.createQrCode(userWithAuthorities.get().getLogin(), "bantheonline", key);
        java.lang.System.out.println( GoogleAuthenticaionUtils.getQRBarcodeURL(userWithAuthorities.get().getLogin(), "bantheonline", key));
        return new TwoFa(uri, key);
    }

    public void confirm2fa(String code, String privateKey) {
        Optional<User> userWithAuthorities = getUserWithAuthorities();
        if (!userWithAuthorities.isPresent()) {
            throw new BadRequestAlertException("Phien lam viec da het han, hay dang nhap lai", "create-account", "logout");
        }
        if (!Strings.isNullOrEmpty(userWithAuthorities.get().getPrivateKey())) {
            throw new BadRequestAlertException("Da tao bao mat 2 lop roi", "authentication", "created");
        }
        try {
            Integer.parseInt(code);
        } catch (Exception ex) {
            throw new BadRequestAlertException("sai ma xac nhan", "authentication", "invalidCode");
        }
        long now = new Date().getTime();
        boolean check = GoogleAuthenticaionUtils.performAuthentication(code, privateKey, 1L, 1);
        if (check) {
            User user = userWithAuthorities.get();
            user.setPrivateKey(privateKey);
            user.setLastVerifiedCode(Integer.parseInt(code));
            user.setLastVerifiedTime(now);
            userRepository.save(user);
            this.clearUserCaches(user);
        } else {
            throw new BadRequestAlertException("sai ma xac nhan", "authentication", "invalidCode");
        }
    }

    public void cancel2fa(String code) {

        Optional<User> userWithAuthorities = getUserWithAuthorities();
        if (!userWithAuthorities.isPresent()) {
            throw new BadRequestAlertException("Phien lam viec da het han, hay dang nhap lai", "create-account", "logout");
        }
        if (Strings.isNullOrEmpty(userWithAuthorities.get().getPrivateKey())) {
            throw new BadRequestAlertException("Da tao bao mat 2 lop roi", "authentication", "notCreated");
        }
        try {
            Integer.parseInt(code);
        } catch (Exception ex) {
            throw new BadRequestAlertException("sai ma xac nhan", "authentication", "invalidCode");
        }
        boolean check = GoogleAuthenticaionUtils.performAuthentication(code, userWithAuthorities.get().getPrivateKey(), 1L, 1);
        if (check) {
            User user = userWithAuthorities.get();
            user.setPrivateKey(null);
            user.setLastVerifiedCode(null);
            user.setLastVerifiedTime(null);
            userRepository.save(user);
            this.clearUserCaches(user);
        } else {
            throw new BadRequestAlertException("sai ma xac nhan", "authentication", "invalidCode");
        }
    }

    public boolean isAuth() {
        Optional<User> userWithAuthorities = getUserWithAuthorities();
        if (!userWithAuthorities.isPresent()) {
            throw new BadRequestAlertException("Phien lam viec da het han, hay dang nhap lai", "create-account", "logout");
        }
        return userRepository.isAuth(userWithAuthorities.get().getId()) == 1L;
    }
}
