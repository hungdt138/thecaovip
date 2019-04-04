package com.hieugie.banthe.service;

import com.hieugie.banthe.web.rest.dto.ReportDTO;
import org.springframework.data.domain.Page;

public interface ReportService {
    ReportDTO getReport(String user, String fromDate, String toDate);
}
