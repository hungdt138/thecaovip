package com.hieugie.banthe.repository;

import com.hieugie.banthe.web.rest.dto.ReportDTO;

public interface ReportRepository {
    ReportDTO getReport(Long id, String fromDate, String toDate);
}
