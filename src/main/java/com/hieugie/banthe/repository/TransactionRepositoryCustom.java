package com.hieugie.banthe.repository;

import com.hieugie.banthe.domain.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

/**
 * Spring Data  repository for the Transaction entity.
 */
@SuppressWarnings("unused")
public interface TransactionRepositoryCustom {

    BigDecimal findAmount(Long id);

    BigDecimal findAvailableAmount(Long id);

    BigDecimal getChargeAmount(Long id);
}
