package com.hieugie.banthe.service;

import com.hieugie.banthe.web.rest.dto.CallBackResponse;
import com.hieugie.banthe.web.rest.dto.ChargeRequestDTO;
import com.hieugie.banthe.web.rest.dto.ChargeResponseDTO;
import com.hieugie.banthe.web.rest.dto.DemandChargeDTO;

public interface ChargeService {
    ChargeResponseDTO charge(ChargeRequestDTO chargeRequestDTO);

    CallBackResponse confirm(DemandChargeDTO chargeRequestDTO);
}
