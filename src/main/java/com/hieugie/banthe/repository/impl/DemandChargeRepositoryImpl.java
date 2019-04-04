package com.hieugie.banthe.repository.impl;

import com.google.common.base.Strings;
import com.hieugie.banthe.domain.DemandCharge;
import com.hieugie.banthe.repository.BillRepositoryCustom;
import com.hieugie.banthe.repository.DemandChargeRepositoryCustom;
import com.hieugie.banthe.web.rest.dto.MyBillDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Repository
public class DemandChargeRepositoryImpl implements DemandChargeRepositoryCustom {

    @Autowired
    private EntityManager entityManager;

    @Override
    public Page<DemandCharge> search(Pageable pageable, String code, String seri) {
        StringBuilder sql = new StringBuilder();
        List<DemandCharge> resultList = new ArrayList<>();
        sql.append("FROM demand_charge WHERE 1 = 1 ");
        if (!Strings.isNullOrEmpty(code)) {
            sql.append("AND code like :code ");
        }

        if (!Strings.isNullOrEmpty(seri)) {
            sql.append("AND serial like :serial ");
        }

        Query countQuery = entityManager.createNativeQuery("SELECT count(1) " + sql.toString());
        if (!Strings.isNullOrEmpty(code)) {
            countQuery.setParameter("code", "%" + code + "%");
        }

        if (!Strings.isNullOrEmpty(seri)) {
            countQuery.setParameter("serial", "%" + seri + "%");
        }
        Number total = (Number) countQuery.getSingleResult();
        if (total.longValue() > 0) {
            Query query = entityManager.createNativeQuery("SELECT * " + sql.toString() + " order by id desc", DemandCharge.class);
            if (!Strings.isNullOrEmpty(code)) {
                query.setParameter("code", "%" + code + "%");
            }

            if (!Strings.isNullOrEmpty(seri)) {
                query.setParameter("serial", "%" + seri + "%");
            }
            query.setMaxResults(pageable.getPageSize());
            query.setFirstResult((int) pageable.getOffset());
            resultList = query.getResultList();
        }
        return new PageImpl<>(resultList, pageable, total.longValue());
    }
}
