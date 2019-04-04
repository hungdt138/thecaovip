package com.hieugie.banthe.repository;

import com.hieugie.banthe.domain.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the Transaction entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long>, TransactionRepositoryCustom {

    @Query("select transaction from Transaction transaction where transaction.fromUser.login = ?#{principal.username}")
    List<Transaction> findByFromUserIsCurrentUser();

    @Query("select transaction from Transaction transaction where transaction.toUser.login = ?#{principal.username}")
    List<Transaction> findByToUserIsCurrentUser();

    Page<Transaction> findByFromUserIdAndStatusNot(Pageable pageable, Long id, int i);
}
