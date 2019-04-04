package com.hieugie.banthe.repository.impl;

import com.hieugie.banthe.domain.Demand;
import com.hieugie.banthe.domain.DemandDtl;
import com.hieugie.banthe.domain.enumeration.NhaMang;
import com.hieugie.banthe.repository.ChargeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Repository
public class ChargeRepositoryImpl implements ChargeRepository {

    @Autowired
    private EntityManager entityManager;

    @Override
    public DemandDtl getChargeCard(NhaMang issuer, BigDecimal price) {
        StringBuilder sql = new StringBuilder();
        sql.append("select c.* from bill a ");
        sql.append("    left join demand b on b.bill_id = a.id ");
        sql.append("    left join demand_dtl c on b.id = c.demand_id ");
        sql.append("WHERE a.status = 1 ");
        sql.append("    AND (b.status is null || b.status != 1) ");
        sql.append("    AND (b.invalid_date is null || DATE_FORMAT(b.invalid_date, '%Y%m%d') <= :now) ");
        sql.append("    AND c.charge_quantity < c.quantity ");
        sql.append("    AND a.type = :issuer ");
        sql.append("    AND c.price = :price ");
        sql.append("    AND (c.status IS NULL OR c.status != 1) ");
        sql.append("    AND b.amount - (SELECT sum(f.real_value) FROM demand_charge f where f.demand_id = b.id and (f.status = 1 or f.status = 3)) >= :price ");
        sql.append("order by a.priority limit 1");
        Query query = entityManager.createNativeQuery(sql.toString(), DemandDtl.class);
        query.setParameter("price", price);
        query.setParameter("now", LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
        query.setParameter("issuer", issuer.name());
        List<DemandDtl> results = query.getResultList();
        if (results.size() > 0) {
            return results.get(0);
        }
        return null;
    }

    @Override
    public Demand getChargeCardNovalue(NhaMang issuer, BigDecimal price, Long id) {
        StringBuilder sql = new StringBuilder();
//        sql.append("select b.* from bill a ");
//        sql.append("    left join demand b on b.bill_id = a.id ");
//        sql.append("WHERE a.status = 1 ");
//        sql.append("    AND (b.status is null || b.status != 1) ");
//        sql.append("    AND a.charge_type = 2  And a.more_fee is null ");
//        sql.append("    AND (b.invalid_date is null || DATE_FORMAT(b.invalid_date, '%Y%m%d') <= :now) ");
//        sql.append("    AND a.type = :issuer ");
//        sql.append("    AND b.amount - (SELECT COALESCE(sum(f.real_value), 0) FROM demand_charge f where f.demand_id = b.id and (f.status = 1 or f.status = 3)) >= :price ");
//        sql.append("order by a.priority limit 1");
        sql.append("select b.* from bill a left join demand b on a.id = b.bill_id left join demand_charge c on a.partner_id = c.partner_id where  c.id = :id");
        Query query = entityManager.createNativeQuery(sql.toString(), Demand.class);
//        query.setParameter("price", price);
//        query.setParameter("issuer", issuer.name());
//        query.setParameter("now", LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
        query.setParameter("id", id);
        List<Demand> results = query.getResultList();
        if (results.size() > 0) {
            return results.get(0);
        }
        return null;
    }
}
