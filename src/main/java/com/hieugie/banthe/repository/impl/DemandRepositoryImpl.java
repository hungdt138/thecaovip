package com.hieugie.banthe.repository.impl;

import com.hieugie.banthe.domain.Demand;
import com.hieugie.banthe.repository.DemandRepositoryCustom;
import com.hieugie.banthe.service.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

public class DemandRepositoryImpl implements DemandRepositoryCustom {

    @Autowired
    private EntityManager entityManager;

    @Override
    public List<Demand> insertBulk(List<Demand> demands) {
        final List<Demand> savedEntities = new ArrayList<>(demands.size());
        int i = 0;
        for (Demand t : demands) {
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
