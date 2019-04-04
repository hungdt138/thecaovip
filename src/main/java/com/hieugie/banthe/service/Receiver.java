package com.hieugie.banthe.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hieugie.banthe.domain.*;
import com.hieugie.banthe.repository.*;
import com.hieugie.banthe.service.util.Common;
import com.hieugie.banthe.service.util.Constants;
import com.hieugie.banthe.web.rest.AccountResource;
import com.hieugie.banthe.web.rest.dto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.lang.System;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Component
public class Receiver {

    private ChargeRepository chargeRepository;
    private RestTemplate restTemplate;
    private DemandRepository demandRepository;
    private UserRepository userRepository;
    private BillRepository billRepository;
    private DemandChargeRepository demandChargeRepository;
    private SystemRepository systemRepository;
    private DemandDtlRepository demandDtlRepository;
    private TransactionRepository transactionRepository;
    private final Logger log = LoggerFactory.getLogger(AccountResource.class);

    public Receiver(ChargeRepository chargeRepository, RestTemplate restTemplate, DemandRepository demandRepository, UserRepository userRepository, BillRepository billRepository, DemandChargeRepository demandChargeRepository, SystemRepository systemRepository, DemandDtlRepository demandDtlRepository, TransactionRepository transactionRepository) {
        this.chargeRepository = chargeRepository;
        this.restTemplate = restTemplate;
        this.demandRepository = demandRepository;
        this.userRepository = userRepository;
        this.billRepository = billRepository;
        this.demandChargeRepository = demandChargeRepository;
        this.systemRepository = systemRepository;
        this.demandDtlRepository = demandDtlRepository;
        this.transactionRepository = transactionRepository;
    }

    public synchronized void receiveMessage(RabbitMQDTO mqdto) {
        StringBuilder message = new StringBuilder();
        ChargeRequestDTO chargeRequestDTO = mqdto.getChargeRequestDTO();
        TrumTheResponse trumTheResponse;
        boolean isError = false;
        boolean change = false;
        boolean somethingWrong = false;
        try {
//            // Lấy ra cái thẻ cần nạp
//            // lưu lại thời gian bắt đầu nạp thẻ
            Optional<DemandCharge> demandChargeOptional = demandChargeRepository.findById(chargeRequestDTO.getId());
            DemandCharge demandCharge = demandChargeOptional.orElseGet(DemandCharge::new);
            demandCharge.setStartDate(Instant.now());

            Demand demand = chargeRepository.getChargeCardNovalue(chargeRequestDTO.getIssuer(), chargeRequestDTO.getInputValue(), chargeRequestDTO.getId());
            Bill bill = null;
            if (demand == null) {
                trumTheResponse = new TrumTheResponse();
                trumTheResponse.setError(-2);
                trumTheResponse.setMsg("Gạch thẻ thất bại");
            } else {
                bill = demand.getBill();
                String chargeUrl;
                int i = 0;
                while (true) {
                    i++;
                    if (i >= 10) {
                        trumTheResponse = new TrumTheResponse();
                        trumTheResponse.setError(-2);
                        trumTheResponse.setMsg("Gạch thẻ thất bại");
                        break;
                    }
                    if ((demand.getBill().getChargeType() == 1 && !change) || (demand.getBill().getChargeType() == 2 && change)) {
                        chargeUrl = Constants.TrumTheApi.TRA_TRUOC + "?phone=" + demand.getAccount() + "&cardcode=" + chargeRequestDTO.getCode();
                    } else {
                        chargeUrl = Constants.TrumTheApi.TRA_SAU
                            + "?newphone=" + demand.getAccount()
                            + "&cardcode=" + chargeRequestDTO.getCode()
                            + "&topuptype=" + demand.getServiceType()
                            + "&logintype=2";
                    }

                    // Nạp sai 5 lần thì chuyển kiểu nạp
                    if (i >= 5 && !change && somethingWrong) {
                        change = true;
                    }

                    try {
                        ResponseEntity<TrumTheResponse> result = restTemplate.postForEntity(chargeUrl, null, TrumTheResponse.class);
                        // Trong trường hợp không trả về response,
                        // ngủ trong vòng 10s và sau đó tiếp tục gọi lại thẻ
                        trumTheResponse = result.getBody();
                        if (trumTheResponse == null) {
                            Thread.sleep(10000);
                        } else if (trumTheResponse.getMsg().equalsIgnoreCase("Thẻ cào nạp thành công cho thuê bao nhưng chưa gạch nợ.")
                            || trumTheResponse.getMsg().toLowerCase().contains(("Thẻ cào đã nạp thành công cho thuê bao nhưng chưa được gạch nợ").toLowerCase())){
                            message.append(trumTheResponse.getMsg()).append(trumTheResponse.getError());
                            isError = true;
                        } else if (trumTheResponse.getError() == 0) {
                            log.info("Nạp thẻ thành công");
                            message.append(trumTheResponse.getMsg()).append(trumTheResponse.getError());
                            break;
                        } else if (trumTheResponse.getError() == 2 && trumTheResponse.getMsg().equalsIgnoreCase("Nạp tiền không thành công.")){
                            somethingWrong = true;
                        } else if (trumTheResponse.getError() == 1 &&
                            (trumTheResponse.getMsg().equalsIgnoreCase("Nạp không thành công vui lòng thử lại") || !trumTheResponse.getMsg().equalsIgnoreCase("Nạp thẻ không thành công, Vui lòng thử lại"))) {
                            log.info("nạp thẻ không thành công vui lòng thử lại");
                            message.append(trumTheResponse.getMsg()).append(trumTheResponse.getError());
                            isError = true;
                        } else if (trumTheResponse.getError() == 1 && (trumTheResponse.getMsg().contains("Tài khoản hoặc mật khẩu đường truyền không đúng") || trumTheResponse.getMsg().contains("Server đăng nhập bị lỗi"))) {
                            message.append(trumTheResponse.getMsg()).append(trumTheResponse.getError());
                        } else if ((trumTheResponse.getError() == 2 && trumTheResponse.getMsg().equalsIgnoreCase("Số điện thoại không phải của Viettel hoặc không có thông tin trên hệ thống."))
                            || (trumTheResponse.getError() == 2 && trumTheResponse.getMsg().equalsIgnoreCase("Quá trình thực hiện có lỗi. Quý khách vui lòng thao tác lại sau. Trân trọng cảm ơn."))
                            || trumTheResponse.getMsg().equalsIgnoreCase("Nạp v2 Chỉ nhận SĐT")) {
                            // Trường hợp số điện thoại định dạng,
                            // Update trạng thái số điện thoại để không lấy ra trong danh sách nạp nữa
                            message.append(trumTheResponse.getMsg()).append(trumTheResponse.getError());
                            log.info("sai số điện thoại");
                            trumTheResponse = new TrumTheResponse();
                            trumTheResponse.setMsg("0");
                            trumTheResponse.setError(-2);
                            demand.setStatus(999);
                            demandRepository.saveAndFlush(demand);
                            cancelBill(bill, chargeRequestDTO.getPartnerId());
                            break;
                        } else if (trumTheResponse.getMsg().equalsIgnoreCase(Constants.TrumTheMessage.QUA_SO_LAN_NAP_SAI)) {
                            // Trường hợp số điện đã vượt quá số lần nạp sai,
                            // Update số điện thoại này đã quá số lần nạp quy định trong ngày hôm nay,
                            // để không lấy ra nạp nữa
                            log.info("Sai quá số lần nạp");
                            message.append(trumTheResponse.getMsg()).append(trumTheResponse.getError());
                            demand.setStatus(2);
                            demand.setInvalidDate(LocalDate.now());
                            demandRepository.saveAndFlush(demand);
                        } else if (trumTheResponse.getError() == 2 &&
                            (trumTheResponse.getMsg().equalsIgnoreCase(Constants.TrumTheMessage.DA_SU_DUNG) || trumTheResponse.getMsg().equalsIgnoreCase(Constants.TrumTheMessage.NAP_KHONG_THANH_CONG))) {
                            message.append(trumTheResponse.getMsg()).append(trumTheResponse.getError());
//                            if (isError) {
//                                trumTheResponse = new TrumTheResponse();
//                                trumTheResponse.setMsg(chargeRequestDTO.getInputValue().toString());
//                                trumTheResponse.setError(0);
//                            }
                            log.info("Thẻ sai hoặc đã được sử dụng");
                            break;
                        } else {
                            message.append(trumTheResponse.getMsg()).append(trumTheResponse.getError());
                        }
                    } catch (Exception ex) {
                        for (int j = 0; j < ex.getStackTrace().length; j++) {
                            log.error(ex.getStackTrace()[j].toString());
                        }
                    }
                }
            }

            // sau khi nhận được giá trị trả về, update thông tin vào db
            // và trả về dữ liệu cho bên cổng game
            int send = 0;
            long value = 0L;
            if (!isError) {
                StringBuilder urlbStringBuilder = new StringBuilder();
                urlbStringBuilder.append(Constants.GameUrl.CALL_BACK_API);

                urlbStringBuilder.append("?username=");
                urlbStringBuilder.append(Constants.GameAuth.USERNAME);
                urlbStringBuilder.append("&");

                urlbStringBuilder.append("password=");
                urlbStringBuilder.append(Constants.GameAuth.PASSWORD);
                urlbStringBuilder.append("&");

                urlbStringBuilder.append("partnerId=");
                urlbStringBuilder.append(chargeRequestDTO.getPartnerId());
                urlbStringBuilder.append("&");

                urlbStringBuilder.append("requestId=");
                urlbStringBuilder.append(chargeRequestDTO.getRequestId());
                urlbStringBuilder.append("&");

                urlbStringBuilder.append("issuer=");
                urlbStringBuilder.append(chargeRequestDTO.getIssuer());
                urlbStringBuilder.append("&");

                urlbStringBuilder.append("serial=");
                urlbStringBuilder.append(chargeRequestDTO.getSerial());
                urlbStringBuilder.append("&");


                String realValue = trumTheResponse.getMsg().replaceAll("\\D+", "");

                try {
                    value = Long.parseLong(realValue);
                } catch (Exception ignored) {

                }

                urlbStringBuilder.append("status=");
                Integer status = null;
                try {
                    if (trumTheResponse.getError() == 0) {
                        if (chargeRequestDTO.getInputValue().longValue() > value) {
                            status = Constants.ChargeStatus.ERROR3;
                        } else {
                            status = Constants.ChargeStatus.OK;
                        }
                        if (change) {
                            bill.setChargeType(bill.getChargeType() == 1 ? 2 : 1);
                        }
                    } else if (trumTheResponse.getError() == 2) {
                        status = Constants.ChargeStatus.ERROR1;
                    } else if (trumTheResponse.getError() == -2) {
                        status = Constants.ChargeStatus.ERROR2;
                    }
                } catch (Exception ex) {
                    status = Constants.ChargeStatus.ERROR2;
                }
                urlbStringBuilder.append(status);
                urlbStringBuilder.append("&");

                urlbStringBuilder.append("inputValue=");
                urlbStringBuilder.append(chargeRequestDTO.getInputValue());
                urlbStringBuilder.append("&");

                urlbStringBuilder.append("realvalue=");
                if (trumTheResponse.getError() == 0) {
                    urlbStringBuilder.append(realValue);
                } else if (trumTheResponse.getError() == 2) {
                    urlbStringBuilder.append(0);
                }

                urlbStringBuilder.append("&");

                urlbStringBuilder.append("datasign=");
                String signa = Constants.GameAuth.USERNAME + Constants.GameAuth.PASSWORD + chargeRequestDTO.getPartnerId()
                    + chargeRequestDTO.getIssuer() + chargeRequestDTO.getRequestId() + chargeRequestDTO.getSerial() + status
                    + chargeRequestDTO.getInputValue() + Constants.PRIVATE_KEY;

                String md5 = Common.createMd5(signa);
                urlbStringBuilder.append(md5);
                System.out.println(md5);
                System.out.println(signa);
                System.out.println(urlbStringBuilder.toString());

                try {
                    ResponseEntity<String> result = restTemplate.getForEntity(urlbStringBuilder.toString(), String.class);
                    ObjectMapper objectMapper = new ObjectMapper();
                    CallBackResponse response = objectMapper.readValue(result.getBody(), CallBackResponse.class);
                    message.append(response.getDescription());
                    System.out.println(response);
                    send = 1;
                } catch (Exception ignored) {
                }

            }
            if (send == 0) {
                demandCharge.setStatus(0);
            } else if (trumTheResponse.getError().equals(0)) {
                demandCharge.setStatus(1);
            } else {
                demandCharge.setStatus(2);
            }
            demandCharge.setDemand(demand);
            demandCharge.setMessage(message.toString());
            demandCharge.setRealValue(BigDecimal.valueOf(value));
            demandChargeRepository.saveAndFlush(demandCharge);

            if (bill != null) {
                if (trumTheResponse.getError() == 0) {
                    // trong trường hợp nạp thẻ thành công
                    // và số tiền của thẻ = số tiền khách hàng nhập
                    // trừ tiền khách hàng
                    com.hieugie.banthe.domain.System system = systemRepository.findByStatusTrue().get();

                    int userPercent;
                    int c1Percent;

                    User user = userRepository.findOneWithAuthoritiesById(demand.getBill().getUser().getId()).get();
                    User daiLyCapTren = null;
                    if (user.getUserId() != null && !Common.isAdmin(user)) {
                        Optional<User> daiLyCapTrenO = userRepository.findOneWithAuthoritiesById(user.getUserId());
                        if (daiLyCapTrenO.isPresent() && !Common.isAdmin(daiLyCapTrenO.get())) {
                            daiLyCapTren = daiLyCapTrenO.get();
                        }
                    }

                    int dayOfMonth = LocalDate.now().getDayOfMonth();

                    // Kiểm tra xem có phải ngày trong trước không
                    if (dayOfMonth >= system.getHighFreeFrom() && dayOfMonth <= system.getHignFeeTo()) {
                        // ngày trong cước

                        if (demand.getBill().getChargeType() == 1) {
                            userPercent = user.getFeePercentLv1b();
                            c1Percent = daiLyCapTren != null && daiLyCapTren.getFeePercentLv1b() != null ? daiLyCapTren.getFeePercentLv1b() : user.getFeePercentLv1b();
                        } else {
                            userPercent = user.getFeePercentLv2b();
                            c1Percent = daiLyCapTren != null && daiLyCapTren.getFeePercentLv2b() != null ? daiLyCapTren.getFeePercentLv2b() : user.getFeePercentLv2b();
                        }
                    } else {
                        // ngày ngoài cước
                        if (demand.getBill().getChargeType() == 1) {
                            userPercent = user.getFeePercentLv1();
                            c1Percent = daiLyCapTren != null && daiLyCapTren.getFeePercentLv1() != null ? daiLyCapTren.getFeePercentLv1() : user.getFeePercentLv1();
                        } else {
                            userPercent = user.getFeePercentLv2();
                            c1Percent = daiLyCapTren != null && daiLyCapTren.getFeePercentLv2() != null ? daiLyCapTren.getFeePercentLv2() : user.getFeePercentLv2();
                        }
                    }

                    // trong trường hợp là nạp trả sau, tích chọn nạp ưu tiên, nạp chọn mệnh giá
                    // mỗi loại + 3%
                    if (demand.getBill().getChargeType() == 2) {
                        if (Boolean.TRUE.equals(demand.getBill().getMoreFee())) {
                            userPercent += 3;
                            c1Percent += 3;
                        }
                        if (Boolean.TRUE.equals(demand.getBill().getHighPriority())) {
                            userPercent += 3;
                            c1Percent += 3;
                        }
                    }

//                    if (system.getViettelPromotionDate()!= null && LocalDate.now().getDayOfMonth() == system.getViettelPromotionDate()) {
//                        userPercent += 3;
//                        c1Percent += 3;
//                    }

                    BigDecimal tienPhanTram = BigDecimal.valueOf(value * userPercent / 100);

                    if (daiLyCapTren != null) {
                        BigDecimal tienPhanTramCt = BigDecimal.valueOf(value * (userPercent - c1Percent) / 100);

                        Transaction transaction = new Transaction();
                        transaction.setStatus(2);
                        transaction.setFromUser(user);
                        transaction.setToUser(daiLyCapTren);
                        transaction.setAmount(tienPhanTramCt);
                        transactionRepository.save(transaction);
                    }

                    // Tạo transaction
                    // tiền 1 trừ vào hệ thống
                    // tiền 2 trừ đẻ cộng cho đại lý cấp trên
                    Transaction transaction = new Transaction();
                    transaction.setStatus(3);
                    transaction.setFromUser(user);
                    transaction.setToUser(null);
                    transaction.setAmount(tienPhanTram);
                    transactionRepository.save(transaction);
                }

                // update số lần nạp không thanh công của thẻ
                // TODO: chỗ này cần sửa lại, đếm theo số lần nạp lỗi liên tiếp
                if (trumTheResponse.getError() != 0) {
                    int chargeErrorCount = 0;
                    List<DemandCharge> chargeError = demandChargeRepository.countByPartnerId(bill.getPartnerId());
                    for (DemandCharge demandCharge1 : chargeError) {
                        if (demandCharge1.getStatus() == 2) {
                            chargeErrorCount++;
                        }
                    }
                    if (chargeErrorCount >= 4) {

                        HttpHeaders headers = new HttpHeaders();
                        headers.setContentType(MediaType.APPLICATION_JSON);
                        ObjectMapper objectMapper = new ObjectMapper();
                        CancelBillDTO cancelBillDTO = new CancelBillDTO();
                        cancelBillDTO.setPartnerID(bill.getPartnerId());
                        String signa2 = bill.getPartnerId() + Constants.PRIVATE_KEY;
                        String md52 = Common.createMd5(signa2);

                        cancelBillDTO.setSignature(md52);
                        String cancelBillJson = objectMapper.writeValueAsString(cancelBillDTO);
                        HttpEntity<String> request = new HttpEntity<>(cancelBillJson, headers);
                        int cancelCount = 0;
                        while (true) {
                            cancelCount++;
                            ResponseEntity<String> cbr = restTemplate.postForEntity(Constants.GameUrl.CANCEL_BILL, request, String.class);
                            CreateBillResponse createBillResponse = objectMapper.readValue(cbr.getBody(), CreateBillResponse.class);
                            if (createBillResponse.getErrorCode() == 1 || createBillResponse.getErrorCode() == -6 || cancelCount > 10) {
                                bill.setStatus(-2);
                                bill.setMessage(createBillResponse.getErrorMsg());
                                break;
                            }
                        }
                    }
                }
                billRepository.saveAndFlush(bill);
            } else {
                cancelBill(null, chargeRequestDTO.getPartnerId());
            }

        } catch (Exception ex) {
            for (int i = 0; i < ex.getStackTrace().length; i++) {
                log.error(ex.getStackTrace()[i].toString());
            }
        }
    }

    private void cancelBill(Bill bill, Long partderId) throws java.io.IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        ObjectMapper objectMapper = new ObjectMapper();
        CancelBillDTO cancelBillDTO = new CancelBillDTO();
        cancelBillDTO.setPartnerID(partderId);
        String signa = partderId + Constants.PRIVATE_KEY;
        String md5 = Common.createMd5(signa);

        cancelBillDTO.setSignature(md5);
        String cancelBillJson = objectMapper.writeValueAsString(cancelBillDTO);
        HttpEntity<String> request = new HttpEntity<>(cancelBillJson, headers);
        int i = 0;
        while (true) {
            if (i > 10) {
                bill.setStatus(-2);
                break;
            }
            i++;
            ResponseEntity<String> cbr = restTemplate.postForEntity(Constants.GameUrl.CANCEL_BILL, request, String.class);
            CreateBillResponse createBillResponse = objectMapper.readValue(cbr.getBody(), CreateBillResponse.class);
            if (createBillResponse.getErrorCode() == 1 || createBillResponse.getErrorCode() == -6) {
                if (bill != null) {
                    bill.setStatus(0);
                }
                break;
            }
        }

    }
}
