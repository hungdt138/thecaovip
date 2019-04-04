package com.hieugie.banthe.repository;

import com.hieugie.banthe.domain.Otp;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Otp entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OtpRepository extends JpaRepository<Otp, Long> {

    @Query("select otp from Otp otp where otp.user.login = ?#{principal.username}")
    List<Otp> findByUserIsCurrentUser();

    @Modifying
    @Query(value = "UPDATE otp SET STATUS = 0 where action = ?1 AND Phone_number = ?2", nativeQuery = true)
    void updateAllOtpByActionAndPhoneNumber(int action, String phoneNumber);

    @Query(value = "select * from otp where code = ?1 and phone_number = ?2 and action = ?3 and status = 1", nativeQuery = true)
    Optional<Otp> findByCodeAndPhoneNumberAndActionAndStatusTrue(String otp, String phoneNumber, int action);
}
