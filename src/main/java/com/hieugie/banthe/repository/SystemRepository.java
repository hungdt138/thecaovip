package com.hieugie.banthe.repository;

import com.hieugie.banthe.domain.System;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.Optional;


/**
 * Spring Data  repository for the System entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SystemRepository extends JpaRepository<System, Long> {

    @Modifying
    @Query(value = "UPDATE jhi_system SET STATUS = 0 WHERE STATUS =1 ", nativeQuery = true)
    void updateStatus();

    Page<System> findAllByStatusTrue(Pageable pageable);

    Optional<System> findByStatusTrue();
}
