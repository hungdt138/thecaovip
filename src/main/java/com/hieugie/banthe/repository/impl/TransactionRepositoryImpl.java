package com.hieugie.banthe.repository.impl;

import com.hieugie.banthe.repository.TransactionRepositoryCustom;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.math.BigDecimal;

public class TransactionRepositoryImpl implements TransactionRepositoryCustom {

    @Autowired
    private EntityManager entityManager;

    @Override
    public BigDecimal findAmount(Long id) {
        String sql = "select sum(case when from_user_id = :userId and status = 1 then ifnull(-amount, 0) " +
            "                when to_user_id = :userId and status = 1 then ifnull(amount, 0) " +
            "                when status = 2 then ifnull(amount, 0) " +
            "                when status = 3 then ifnull(-amount, 0) " +
            "          end) from transaction where from_user_id = :userId or to_user_id = :userId";
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("userId", id);
        return (BigDecimal) query.getSingleResult();
    }

    @Override
    public BigDecimal findAvailableAmount(Long id) {
        String sql = "select sum(amount) " +
            "from ((select sum(case " +
            "                   when from_user_id = :userId and status = 1 then ifnull(-amount, 0) " +
            "                   when to_user_id = :userId and status = 1 then ifnull(amount, 0) " +
            "                   when status = 2 then ifnull(amount, 0) " +
            "                   when status = 3 then ifnull(-amount, 0) end) amount " +
            "      from transaction " +
            "      where from_user_id = :userId " +
            "         or to_user_id = :userId) " +
            "union " +
            "(select sum(amount) amount " +
            " from ( " +
            "        select type, " +
            "               status, " +
            "               sum((case when status = 1 and ifnull(chargedAmount, 0) <= b.amount then (ifnull(chargedAmount, 0) - b.amount) end) / 100 * (case " +
            "                                                                                         when type = 'VTT' and charge_type = 1 " +
            "                                                                                           then c.fee_percent_lv_1 " +
            "                                                                                         when type = 'VTT' and charge_type = 2 " +
            "                                                                                           then c.fee_percent_lv_2 end)) amount " +
            "        from bill b " +
            "               left join (select partner_id, sum(ifnull(real_value, 0)) chargedAmount " +
            "                           from demand_charge a " +
            "                           where a.status != 2 " +
            "                             and status < 6 " +
            "                             and partner_id in (select partner_id from bill where user_id = :userId) " +
            "                           group by partner_id) a on b.partner_id = a.partner_id " +
            "               left join jhi_user c on b.user_id = c.id " +
            "        where b.user_id = :userId) a)) a";
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("userId", id);
        return (BigDecimal) query.getSingleResult();
    }

    @Override
    public BigDecimal getChargeAmount(Long id) {
        String sql = "select sum(amount) from ( " +
            "(select ifnull(sum(ifnull(amount, 0)),0) amount " +
            "from bill where (status = 1 or status = 2) and user_id = :userId) " +
            "union all " +
            "(select -ifnull(sum(ifnull(real_value, 0)), 0) amount from demand_charge a " +
            "  left join bill b on a.partner_id = b.partner_id " +
            "where (b.status = 1 or b.status = 2) " +
            "  and (a.status = 1 or a.status = 3) " +
            "  and b.user_id = :userId)) as baaba ";
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("userId", id);
        return (BigDecimal) query.getSingleResult();
    }
}
