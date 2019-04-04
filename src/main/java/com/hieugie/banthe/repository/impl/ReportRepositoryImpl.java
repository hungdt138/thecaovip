package com.hieugie.banthe.repository.impl;

import com.google.common.base.Strings;
import com.hieugie.banthe.domain.Demand;
import com.hieugie.banthe.domain.DemandDtl;
import com.hieugie.banthe.domain.enumeration.NhaMang;
import com.hieugie.banthe.repository.ChargeRepository;
import com.hieugie.banthe.repository.ReportRepository;
import com.hieugie.banthe.web.rest.dto.ReportDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Repository
public class ReportRepositoryImpl implements ReportRepository {

    @Autowired
    private EntityManager entityManager;

    @Override
    public ReportDTO getReport(Long id, String fromDate, String toDate) {
        StringBuilder sql = new StringBuilder();
        sql.append("select sum(chargedAmount), sum(case when chargedAmount > amount then amount else chargedAmount end), ");
        sql.append("sum(case when chargedAmount > amount and b.charge_type = 1 then amount when chargedAmount <= amount and b.charge_type = 1 then chargedAmount end), ");
        sql.append("sum(case when chargedAmount > amount and b.charge_type = 2 then amount when chargedAmount <= amount and b.charge_type = 2 then chargedAmount end) ");
        sql.append(" from bill b inner join ");
        sql.append("  (select partner_id, sum(ifnull(real_value, 0)) chargedAmount ");
        sql.append("from demand_charge a where a.status != 2 ");
        if (!Strings.isNullOrEmpty(fromDate)) {
            sql.append("AND DATE_FORMAT(DATE_ADD(a.created_date, INTERVAL 7 HOUR), '%Y%m%d') >= :fromDate ");
        }
        if (!Strings.isNullOrEmpty(toDate)) {
            sql.append("AND DATE_FORMAT(DATE_ADD(a.created_date, INTERVAL 7 HOUR), '%Y%m%d') <= :toDate ");
        }
        sql.append("and partner_id in (select partner_id from bill where user_id = :userId) group by partner_id) ");
        sql.append(" a on b.partner_id = a.partner_id");
        Query query = entityManager.createNativeQuery(sql.toString());
        query.setParameter("userId", id);
        if (!Strings.isNullOrEmpty(fromDate)) {
            query.setParameter("fromDate", fromDate);
        }
        if (!Strings.isNullOrEmpty(toDate)) {
            query.setParameter("toDate", toDate);
        }

        List<Object[]> resultList = query.getResultList();
        ReportDTO reportDTO = new ReportDTO();
        if (resultList.size() > 0) {
            reportDTO.setChargedAmount((BigDecimal) resultList.get(0)[1]);
            reportDTO.setRealAmount((BigDecimal) resultList.get(0)[0]);
            reportDTO.setPrePay((BigDecimal) resultList.get(0)[2]);
            reportDTO.setAfterPay((BigDecimal) resultList.get(0)[3]);
//            reportDTO.setAfterFeeAmount(((BigDecimal) resultList.get(0)[1]).multiply(BigDecimal.valueOf(75)).divide(BigDecimal.valueOf(100), 0));

        }
        return reportDTO;
    }
}
