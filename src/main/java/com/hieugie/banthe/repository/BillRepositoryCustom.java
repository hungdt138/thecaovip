package com.hieugie.banthe.repository;

import com.hieugie.banthe.web.rest.dto.MyBillDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


/**
 * Spring Data  repository for the Bill entity.
 */
@SuppressWarnings("unused")
public interface BillRepositoryCustom {

    Page<MyBillDTO> findByUserId(Pageable pageable, String account, Long id, String partnerId, String fullName, Integer status);

    MyBillDTO findOneById(Long id);
}
