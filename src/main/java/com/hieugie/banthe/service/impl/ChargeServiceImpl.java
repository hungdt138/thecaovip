package com.hieugie.banthe.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hieugie.banthe.domain.Bill;
import com.hieugie.banthe.domain.Demand;
import com.hieugie.banthe.domain.DemandCharge;
import com.hieugie.banthe.domain.DemandDtl;
import com.hieugie.banthe.domain.enumeration.NhaMang;
import com.hieugie.banthe.repository.*;
import com.hieugie.banthe.service.ChargeService;
import com.hieugie.banthe.service.util.Common;
import com.hieugie.banthe.service.util.Constants;
import com.hieugie.banthe.web.rest.dto.*;
import com.hieugie.banthe.web.rest.errors.BadRequestAlertException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Optional;

@Service
@Transactional
public class ChargeServiceImpl implements ChargeService {

    @Autowired
    private ChargeRepository chargeRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private DemandRepository demandRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BillRepository billRepository;

    @Autowired
    @Qualifier("taskExecutor")
    private ThreadPoolTaskExecutor taskExecutor;

    @Autowired
    private DemandChargeRepository demandChargeRepository;

    @Autowired
    private SystemRepository systemRepository;

    @Autowired
    private DemandDtlRepository demandDtlRepository;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public ChargeResponseDTO charge(ChargeRequestDTO chargeRequestDTO) {

        // kiểm tra chữ ký
        String signa = chargeRequestDTO.getPartnerId() + chargeRequestDTO.getRequestId() + chargeRequestDTO.getCode() + chargeRequestDTO.getSerial() + chargeRequestDTO.getInputValue();
//        if (!Common.createMd5(signa).equalsIgnoreCase(chargeRequestDTO.getSignature())) {
//            ChargeResponseDTO responseDTO = new ChargeResponseDTO();
//            responseDTO.setDescription("Sai chữ ký");
//            responseDTO.setStatus(-1);
//            responseDTO.setRequestId(chargeRequestDTO.getRequestId());
//            return responseDTO;
//        }
        // Kiểm tra trong db xem thẻ này đã được nạp hay chưa
        Optional<DemandCharge> byCode = demandChargeRepository.findByCode(chargeRequestDTO.getCode());
        if (byCode.isPresent()) {

            ChargeResponseDTO responseDTO = new ChargeResponseDTO();
            responseDTO.setDescription("Thẻ đã bị sử dụng trước đó");
            responseDTO.setStatus(3);
            responseDTO.setRequestId(chargeRequestDTO.getRequestId());
            return responseDTO;
        }

        // kiểm tra đơn hàng có tồn tại hay không
//        Bill bill = billRepository.findByPartnerId(chargeRequestDTO.getPartnerId());
//        if (bill == null) {
//            ChargeResponseDTO responseDTO = new ChargeResponseDTO();
//            responseDTO.setDescription("Không tìm thấy đơn hàng");
//            responseDTO.setStatus(2);
//            responseDTO.setRequestId(chargeRequestDTO.getRequestId());
//            return responseDTO;
//        }

        DemandCharge demandCharge = new DemandCharge();
        demandCharge.setStatus(0);
        demandCharge.setRequestId(chargeRequestDTO.getRequestId());
        demandCharge.setInputValue(chargeRequestDTO.getInputValue());
        demandCharge.setSerial(chargeRequestDTO.getSerial());
        demandCharge.setCode(chargeRequestDTO.getCode());
        demandCharge.setPartnerId(chargeRequestDTO.getPartnerId());
        demandCharge.setType(chargeRequestDTO.getIssuer());
        demandCharge = demandChargeRepository.save(demandCharge);

        if (demandCharge.getType().equals(NhaMang.VTT)) {
            RabbitMQDTO mqdto = new RabbitMQDTO();
            chargeRequestDTO.setId(demandCharge.getId());
            mqdto.setChargeRequestDTO(chargeRequestDTO);
            rabbitTemplate.convertAndSend("thevip", "nhanTheRoutingKey", mqdto);
        }


        ChargeResponseDTO responseDTO = new ChargeResponseDTO();
        responseDTO.setDescription("Giao dịch ghi nhận thành công, vui lòng đợi kết quả");
        responseDTO.setStatus(100);
        responseDTO.setRequestId(chargeRequestDTO.getRequestId());
        System.out.println("Giao dich thanh cong");
        return responseDTO;

    }

    @Override
    public CallBackResponse confirm(DemandChargeDTO dc) {
        Optional<DemandCharge> demandCharge2 = demandChargeRepository.findById(dc.getId());
        if (!demandCharge2.isPresent()) {
            throw new BadRequestAlertException("loi", "loi", "loi");
        }
        DemandCharge demandCharge = demandCharge2.get();
        // Tao call back
        StringBuilder urlbStringBuilder = new StringBuilder();
        urlbStringBuilder.append(Constants.GameUrl.CALL_BACK_API);

        urlbStringBuilder.append("?username=");
        urlbStringBuilder.append(Constants.GameAuth.USERNAME);
        urlbStringBuilder.append("&");

        urlbStringBuilder.append("password=");
        urlbStringBuilder.append(Constants.GameAuth.PASSWORD);
        urlbStringBuilder.append("&");

        urlbStringBuilder.append("partnerId=");
        urlbStringBuilder.append(demandCharge.getPartnerId());
        urlbStringBuilder.append("&");

        urlbStringBuilder.append("requestId=");
        urlbStringBuilder.append(demandCharge.getRequestId());
        urlbStringBuilder.append("&");

        urlbStringBuilder.append("issuer=");
        urlbStringBuilder.append(demandCharge.getType().toString());
        urlbStringBuilder.append("&");

        urlbStringBuilder.append("serial=");
        urlbStringBuilder.append(demandCharge.getSerial());
        urlbStringBuilder.append("&");

        urlbStringBuilder.append("status=");
        urlbStringBuilder.append(dc.getStatus());
        urlbStringBuilder.append("&");

        urlbStringBuilder.append("inputValue=");
        urlbStringBuilder.append(demandCharge.getInputValue().longValue());
        urlbStringBuilder.append("&");

        urlbStringBuilder.append("realvalue=");
        if (dc.getRealValue() != null) {
            urlbStringBuilder.append(dc.getRealValue().longValue());
        } else {
            urlbStringBuilder.append(0);
        }
        urlbStringBuilder.append("&");
        urlbStringBuilder.append("datasign=");


        String signa = Constants.GameAuth.USERNAME + Constants.GameAuth.PASSWORD + demandCharge.getPartnerId()
            + demandCharge.getType().toString() + demandCharge.getRequestId() + demandCharge.getSerial() + dc.getStatus()
            + demandCharge.getInputValue().longValue() + Constants.PRIVATE_KEY;
        System.out.println(signa);
        String md5 = Common.createMd5(signa);
        System.out.println(md5);
        urlbStringBuilder.append(md5);
        System.out.println(urlbStringBuilder.toString());

        try {
            ResponseEntity<String> result = restTemplate.getForEntity(urlbStringBuilder.toString(), String.class);
            ObjectMapper objectMapper = new ObjectMapper();

            CallBackResponse response = objectMapper.readValue(result.getBody(), CallBackResponse.class);
            if (response.getStatus() == 1 && dc.getStatus() == 1) {
                demandCharge.setRealValue(dc.getRealValue());
                // update số tiền đã nạp được cho demand và bill
                Demand demand = demandCharge.getDemand();
                if (demand != null) {
                    demand.setChargedAmount(demand.getChargedAmount().add(demandCharge.getRealValue()));
                    demandRepository.save(demand);

                    Bill bill = demand.getBill();
                    bill.setChargedAmount(bill.getChargedAmount().add(demandCharge.getRealValue()));
                    if (bill.getChargedAmount().compareTo(bill.getAmount()) == 0) {
                        bill.setStatus(2);
                    }
                    billRepository.save(bill);

                    DemandDtl demandDtl = demandDtlRepository.findByDemandIdAndPrice(demand.getId(), demandCharge.getInputValue());

                    // Update số lượng đã nạp
                    if (demandDtl != null) {
                        demandDtl.setChargeQuantity(demandDtl.getChargeQuantity() + 1);
                        demandDtlRepository.save(demandDtl);
                    }
                }
            }
            demandCharge.setStatus(dc.getStatus());
            demandChargeRepository.save(demandCharge);
            return response;
        } catch (Exception e) {
            e.printStackTrace();
        }
        throw new BadRequestAlertException("loi", "loi", "loi");
    }

    @Async("taskExecutor")
    public void asyncMethodWithConfiguredExecutor() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Execute method with configured executor - "
            + Thread.currentThread().getName());

    }
}
