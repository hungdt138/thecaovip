package com.hieugie.banthe.repository.impl;

import com.hieugie.banthe.domain.DemandDtl;
import com.hieugie.banthe.repository.DemandDtlRepositoryCustom;
import com.hieugie.banthe.service.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

public class DemandDtlRepositoryImpl implements DemandDtlRepositoryCustom {

    @Autowired
    private EntityManager entityManager;

    @Override
    public List<DemandDtl> insertBulk(List<DemandDtl> demandDtls) {
        final List<DemandDtl> savedEntities = new ArrayList<>(demandDtls.size());
        int i = 0;
        for (DemandDtl t : demandDtls) {
            entityManager.persist(t);
            savedEntities.add(t);
            i++;
            if (i > Constants.BATCH_SIZE) {
                entityManager.flush();
                entityManager.clear();
                i = 0;
            }
        }
        if (i > 0) {
            entityManager.flush();
            entityManager.clear();
        }

        return savedEntities;

    }
}
