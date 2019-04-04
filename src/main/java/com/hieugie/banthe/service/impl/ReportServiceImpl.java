package com.hieugie.banthe.service.impl;

import com.google.common.base.Strings;
import com.hieugie.banthe.domain.User;
import com.hieugie.banthe.repository.ReportRepository;
import com.hieugie.banthe.repository.UserRepository;
import com.hieugie.banthe.service.ReportService;
import com.hieugie.banthe.service.UserService;
import com.hieugie.banthe.web.rest.dto.ReportDTO;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ReportServiceImpl implements ReportService {

    private final UserRepository userRepository;

    private final UserService userService;

    private final ReportRepository reportRepository;

    public ReportServiceImpl(UserRepository userRepository, UserService userService, ReportRepository reportRepository) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.reportRepository = reportRepository;
    }

    @Override
    public ReportDTO getReport(String userLogin, String fromDate, String toDate) {
        User user = null;
        if (!Strings.isNullOrEmpty(userLogin)) {
            Optional<User> oneByLogin = userRepository.findOneByLogin(userLogin);
            if (oneByLogin.isPresent()) {
                user = oneByLogin.get();
            }
        } else {
            Optional<User> userWithAuthorities = userService.getUserWithAuthorities();
            if (userWithAuthorities.isPresent()) {
                user = userWithAuthorities.get();
            }
        }
        if (user == null) {
            return null;
        }

        return reportRepository.getReport(user.getId(), fromDate, toDate);
    }
}
