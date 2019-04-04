package com.hieugie.banthe.repository;

import com.hieugie.banthe.domain.Demand;

import java.util.List;

public interface DemandRepositoryCustom {
    List<Demand> insertBulk(List<Demand> demands);
}
