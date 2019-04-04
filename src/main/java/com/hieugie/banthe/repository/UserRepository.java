package com.hieugie.banthe.repository;

import com.hieugie.banthe.domain.User;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.time.Instant;

/**
 * Spring Data JPA repository for the User entity.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    String USERS_BY_LOGIN_CACHE = "usersByLogin";

    String USERS_BY_EMAIL_CACHE = "usersByEmail";

    Optional<User> findOneByActivationKey(String activationKey);

    List<User> findAllByActivatedIsFalseAndCreatedDateBefore(Instant dateTime);

    Optional<User> findOneByResetKey(String resetKey);

    Optional<User> findOneByEmailIgnoreCase(String email);

    Optional<User> findOneByLogin(String login);

    @EntityGraph(attributePaths = "authorities")
    Optional<User> findOneWithAuthoritiesById(Long id);

    @EntityGraph(attributePaths = "authorities")
    @Cacheable(cacheNames = USERS_BY_LOGIN_CACHE)
    Optional<User> findOneWithAuthoritiesByLogin(String login);

    @EntityGraph(attributePaths = "authorities")
    @Cacheable(cacheNames = USERS_BY_EMAIL_CACHE)
    Optional<User> findOneWithAuthoritiesByEmail(String email);

    Page<User> findAllByLoginNot(Pageable pageable, String login);

    Page<User> findByUserId(Pageable pageable, Long id);

    Optional<Object> findOneByPhoneNumber(String phoneNumber);

    @Modifying
    @Query(value = "update jhi_user set amount = ?1, available_amount = ?2 where id = ?3", nativeQuery = true)
    void updateAmount(Long amount, Long availableAmount, Long id);

    @Query(value = "SELECT IFNULL(AVAILABLE_AMOUNT, 0) FROM jhi_user WHERE ID = ?1", nativeQuery = true)
    BigDecimal findAvailableAmount(Long Id);

    @Query(value = "select (case when private_key is null then 0 else 1 end)  from jhi_user where id = ?1", nativeQuery = true)
    Long isAuth(Long id);
}
