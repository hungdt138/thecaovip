package com.hieugie.banthe.repository.impl;

import com.google.common.base.Strings;
import com.hieugie.banthe.repository.BillRepositoryCustom;
import com.hieugie.banthe.service.util.Common;
import com.hieugie.banthe.web.rest.dto.MyBillDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Repository
public class BillRepositoryImpl implements BillRepositoryCustom {

    @Autowired
    private EntityManager entityManager;

    @Override
    public Page<MyBillDTO> findByUserId(Pageable pageable, String account, Long id, String partnerId, String fullName, Integer status) {
        List<MyBillDTO> myBillDTOS = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        String selectType = "";

        String select = "from (select a.name, a.amount, a.type, a.priority, a.charge_type, " +
            "(SELECT COALESCE(sum(f.real_value), 0) FROM demand_charge f left join demand d on f.demand_id = d.id where d.bill_id = a.id) chargedAmount," +
            " a.partner_id, a.id, b.jhi_account, a.status, c.login,  DATE_FORMAT(a.created_Date, '%H:%i %d/%m/%Y') ";
        sql.append("from bill a left join demand b on a.id = b.bill_id ");
        sql.append("left join jhi_user c on a.user_id = c.id ");
        sql.append(" where 1 = 1 ");
        if (id != null) {
            sql.append("AND a.user_id = :id ");
        }
        if (!Strings.isNullOrEmpty(account)) {
            sql.append("AND b.jhi_account like :account ");
        }

        if (!Strings.isNullOrEmpty(partnerId)) {
            sql.append("AND a.partner_id = :partnerId ");
        }

        if (!Strings.isNullOrEmpty(fullName)) {
            sql.append("AND c.login like :fullName ");
        }

        if (status != null) {
            if (status == 1) {
                sql.append(") a where amount > chargedAmount and (status = 1 or status = 2)");
            } else if (status == 2) {
                sql.append(") a where amount <= chargedAmount and (status = 1 or status = 2)");
            } else {
                sql.append(") a where status != 1 and amount > chargedAmount");
            }
        } else {
            sql.append(") a ");
        }


        Query countQuery = entityManager.createNativeQuery("select count(1) " + select + sql);
        if (id != null) {
            countQuery.setParameter("id", id);
        }
        if (!Strings.isNullOrEmpty(account)) {
            countQuery.setParameter("account", "%" + account + "%");
        }
        if (!Strings.isNullOrEmpty(partnerId)) {
            countQuery.setParameter("partnerId", partnerId);
        }
        if (!Strings.isNullOrEmpty(fullName)) {
            countQuery.setParameter("fullName", "%" + fullName + "%");
        }
        Number total = (Number) countQuery.getSingleResult();
        if (total.longValue() > 0) {
            Query query = entityManager.createNativeQuery("select * " + select + sql.toString() + " order by id desc");
            if (id != null) {
                query.setParameter("id", id);
            }
            if (!Strings.isNullOrEmpty(account)) {
                query.setParameter("account", "%" + account + "%");
            }
            if (!Strings.isNullOrEmpty(fullName)) {
                query.setParameter("fullName", "%" + fullName + "%");
            }
            if (!Strings.isNullOrEmpty(partnerId)) {
                query.setParameter("partnerId", partnerId);
            }
            query.setMaxResults(pageable.getPageSize());
            query.setFirstResult((int) pageable.getOffset());
            List<Object[]> resultList = query.getResultList();
            resultList.forEach(object -> {
                MyBillDTO myBillDTO = new MyBillDTO();
                getMyBillDTO(myBillDTO, object);
                myBillDTOS.add(myBillDTO);
            });
        }
        return new PageImpl<>(myBillDTOS, pageable, total.longValue());
    }

    @Override
    public MyBillDTO findOneById(Long id) {
        StringBuilder sql = new StringBuilder();
        String select = "select a.name, a.amount, a.type, a.priority, a.charge_type, " +
            "(SELECT COALESCE(sum(f.real_value), 0) FROM demand_charge f left join demand d on f.demand_id = d.id where d.bill_id = a.id) chargedAmount," +
            " a.partner_id, a.id, b.jhi_account, a.status, c.login ";
        sql.append("from bill a left join demand b on a.id = b.bill_id ");
        sql.append("left join jhi_user c on a.user_id = c.id ");
        sql.append(" where b.id = :id ");

        Query query = entityManager.createNativeQuery(select + sql.toString() + " order by id desc");
        if (id != null) {
            query.setParameter("id", id);
        }
        MyBillDTO myBillDTO = new MyBillDTO();
        List<Object[]> resultList = query.getResultList();
        resultList.forEach(object -> {
            getMyBillDTO(myBillDTO, object);
        });
        return myBillDTO;
    }


    private MyBillDTO getMyBillDTO(MyBillDTO myBillDTO, Object[] object) {
        myBillDTO.setName((String) object[0]);
        myBillDTO.setAmount(Common.getBigDecimal(object[1]));
        myBillDTO.setType((String) object[2]);
        myBillDTO.setPriority((Integer) object[3]);
        myBillDTO.setChargeType(((Integer) object[4]));
        myBillDTO.setChargedAmount(Common.getBigDecimal(object[5]));
        myBillDTO.setPartnerId(object[6] != null ? ((BigInteger) object[6]).longValue() : 0);
        myBillDTO.setId(((BigInteger) object[7]).longValue());
        myBillDTO.setAccount((String) object[8]);
        myBillDTO.setStatus((Integer) object[9]);
        myBillDTO.setFullName((String) object[10]);
        myBillDTO.setCreatedDate((String) object[11]);
        return myBillDTO;
    }
}
