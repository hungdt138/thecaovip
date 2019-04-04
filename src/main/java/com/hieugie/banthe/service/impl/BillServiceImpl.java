package com.hieugie.banthe.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import com.hieugie.banthe.domain.System;
import com.hieugie.banthe.domain.*;
import com.hieugie.banthe.domain.enumeration.NhaMang;
import com.hieugie.banthe.repository.*;
import com.hieugie.banthe.security.AuthoritiesConstants;
import com.hieugie.banthe.service.BillService;
import com.hieugie.banthe.service.UserService;
import com.hieugie.banthe.service.util.Common;
import com.hieugie.banthe.service.util.Constants;
import com.hieugie.banthe.service.util.ExcelUtils;
import com.hieugie.banthe.web.rest.dto.*;
import com.hieugie.banthe.web.rest.errors.BadRequestAlertException;
import net.logstash.logback.encoder.org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Service Implementation for managing Bill.
 */
@Service
@Transactional
public class BillServiceImpl implements BillService {

    private final Logger log = LoggerFactory.getLogger(BillServiceImpl.class);

    private final BillRepository billRepository;

    private final DemandRepository demandRepository;

    private final DemandDtlRepository demandDtlRepository;

    private final UserService userService;

    private final RestTemplate restTemplate;

    private final SystemRepository systemRepository;

    private final UserRepository userRepository;

    private final DemandChargeRepository demandChargeRepository;

    private final TransactionRepository transactionRepository;

    public BillServiceImpl(BillRepository billRepository, DemandRepository demandRepository, DemandDtlRepository demandDtlRepository, UserService userService, RestTemplate restTemplate, SystemRepository systemRepository, UserRepository userRepository, DemandChargeRepository demandChargeRepository, TransactionRepository transactionRepository) {
        this.billRepository = billRepository;
        this.demandRepository = demandRepository;
        this.demandDtlRepository = demandDtlRepository;
        this.userService = userService;
        this.restTemplate = restTemplate;
        this.systemRepository = systemRepository;
        this.userRepository = userRepository;
        this.demandChargeRepository = demandChargeRepository;
        this.transactionRepository = transactionRepository;
    }

    /**
     * Save a bill.
     *
     * @param bill the entity to save
     * @return the persisted entity
     */
    @Override
    public Bill save(BillDTO bill) {
        log.debug("Request to save Bill : {}", bill);

        // Kiểm tra xem hệ thống có nhận đơn hàng
        Optional<System> byStatusTrue = systemRepository.findByStatusTrue();
        if (!byStatusTrue.isPresent()) {
            throw new BadRequestAlertException("co loi he thong", "bill", "invalidSystem");
        }

        if (bill.getBill().getType().equals(NhaMang.VTT) && Boolean.TRUE.equals(byStatusTrue.get().getLockViettel())) {
            throw new BadRequestAlertException("He thong nhung nhan don hàng cua nha mang nay", "bill", "lockBill");
        }

        if (bill.getBill().getType().equals(NhaMang.VNP) && Boolean.TRUE.equals(byStatusTrue.get().getLockVina())) {
            throw new BadRequestAlertException("He thong nhung nhan don hàng cua nha mang nay", "bill", "lockBill");
        }

        if (bill.getBill().getType().equals(NhaMang.VMS) && Boolean.TRUE.equals(byStatusTrue.get().getLockMobi())) {
            throw new BadRequestAlertException("He thong nhung nhan don hàng cua nha mang nay", "bill", "lockBill");
        }

        Optional<User> userOptional = userService.getUserWithAuthorities();
        if (userOptional.isPresent()) {

            User user = userOptional.get();


            Bill myBill = bill.getBill();

            // Check độ ưu tiên đơn hàng,
            // Nếu như user tạo đơn hàng là admin thì độ ưu tiên tao nhất
            if (Common.isAdmin(user)) {
                myBill.setPriority(1);
            } else if (myBill.getChargeType() == 1) {
                // Thẻ trả trước ưu tiên thứ 2
                myBill.setPriority(2);
            } else if (myBill.getChargeType() == 2 && Boolean.TRUE.equals(myBill.getHighPriority())) {
                // thẻ trả sau và muốn ưu tiên thứ 3
                myBill.setPriority(3);
            } else if (myBill.getChargeType() == 2 && !Boolean.TRUE.equals(myBill.getHighPriority())) {
                // thẻ trả sau nhưng không ưu tiên thứ 4
                myBill.setPriority(4);
            } else {
                // thẻ siêu rẻ ưu tiên 5
                myBill.setPriority(5);
            }

            myBill.setStatus(1);

            myBill.setUser(user);

            List<Demand> demands = bill.getDemands();
            // duyệt qua từng nhu cầu nạp thẻ 1 để phân loại thẻ nạp
            // theo mệnh giá 1.000.000 -> 10.000 VND
            myBill.setChargedAmount(BigDecimal.ZERO);
            int sum = demands.stream().mapToInt(o -> o.getAmount().intValue()).sum();
            BigDecimal availableAmount = transactionRepository.findAvailableAmount(user.getId());
            if (!Common.isAdmin(user) && (availableAmount == null || sum > availableAmount.intValue())) {
                throw new BadRequestAlertException("Tai khoan khong du so du de tao don hang nay", "bill", "invalidAmount");
            }
            myBill.setAmount(BigDecimal.valueOf(sum));
            myBill = billRepository.save(myBill);

            int quantity10 = 0;
            int quantity20 = 0;
            int quantity50 = 0;
            int quantity100 = 0;
            int quantity200 = 0;
            int quantity500 = 0;
            int quantity1000 = 0;


            for (Demand demand : demands) {
                demand.setId(null);
                demand.setBill(myBill);
                demand.setChargedAmount(BigDecimal.ZERO);
                demand.setAccount(StringUtils.deleteWhitespace(demand.getAccount()));
            }
            demands = demandRepository.insertBulk(demands);
            List<DemandDtl> demandDtls = new ArrayList<>();

            // Nếu là thẻ trả sau mà không tích chọn nạp mênh giá cáo
            // thì không cần tạo demand dtl
            if (Boolean.TRUE.equals(myBill.getMoreFee())) {
                for (Demand demand : demands) {
                    BigDecimal amount = demand.getAmount();
                    if (amount.compareTo(Constants.MENH_GIA_THE.MOT_TRIEU) >= 0) {
                        long phanNguyen = amount.longValue() / Constants.MENH_GIA_THE.MOT_TRIEU.longValue();
                        DemandDtl dtl = new DemandDtl();
                        dtl.setDemand(demand);
                        dtl.setPrice(Constants.MENH_GIA_THE.MOT_TRIEU);
                        dtl.setQuantity((int) phanNguyen);
                        demandDtls.add(dtl);
                        quantity1000 += (int) phanNguyen;
                        amount = amount.subtract(Constants.MENH_GIA_THE.MOT_TRIEU.multiply(BigDecimal.valueOf(phanNguyen)));
                    }

                    if (amount.compareTo(Constants.MENH_GIA_THE.NAM_TRAM) >= 0) {
                        long phanNguyen = amount.longValue() / Constants.MENH_GIA_THE.NAM_TRAM.longValue();
                        DemandDtl dtl = new DemandDtl();
                        dtl.setDemand(demand);
                        dtl.setPrice(Constants.MENH_GIA_THE.NAM_TRAM);
                        dtl.setQuantity((int) phanNguyen);
                        demandDtls.add(dtl);
                        quantity500 += (int) phanNguyen;
                        amount = amount.subtract(Constants.MENH_GIA_THE.NAM_TRAM.multiply(BigDecimal.valueOf(phanNguyen)));
                    }

                    if (amount.compareTo(Constants.MENH_GIA_THE.HAI_TRAM) >= 0) {
                        long phanNguyen = amount.longValue() / Constants.MENH_GIA_THE.HAI_TRAM.longValue();
                        DemandDtl dtl = new DemandDtl();
                        dtl.setDemand(demand);
                        dtl.setPrice(Constants.MENH_GIA_THE.HAI_TRAM);
                        dtl.setQuantity((int) phanNguyen);
                        quantity200 += (int) phanNguyen;
                        demandDtls.add(dtl);

                        amount = amount.subtract(Constants.MENH_GIA_THE.HAI_TRAM.multiply(BigDecimal.valueOf(phanNguyen)));
                    }

                    if (amount.compareTo(Constants.MENH_GIA_THE.MOT_TRAM) >= 0) {
                        long phanNguyen = amount.longValue() / Constants.MENH_GIA_THE.MOT_TRAM.longValue();
                        DemandDtl dtl = new DemandDtl();
                        dtl.setDemand(demand);
                        dtl.setPrice(Constants.MENH_GIA_THE.MOT_TRAM);
                        dtl.setQuantity((int) phanNguyen);
                        demandDtls.add(dtl);
                        quantity100 += (int) phanNguyen;
                        amount = amount.subtract(Constants.MENH_GIA_THE.MOT_TRAM.multiply(BigDecimal.valueOf(phanNguyen)));
                    }

                    if (amount.compareTo(Constants.MENH_GIA_THE.NAM_CHUC) >= 0) {
                        long phanNguyen = amount.longValue() / Constants.MENH_GIA_THE.NAM_CHUC.longValue();
                        DemandDtl dtl = new DemandDtl();
                        dtl.setDemand(demand);
                        dtl.setPrice(Constants.MENH_GIA_THE.NAM_CHUC);
                        dtl.setQuantity((int) phanNguyen);
                        demandDtls.add(dtl);
                        quantity50 += (int) phanNguyen;
                        amount = amount.subtract(Constants.MENH_GIA_THE.NAM_CHUC.multiply(BigDecimal.valueOf(phanNguyen)));
                    }

                    if (amount.compareTo(Constants.MENH_GIA_THE.HAI_CHUC) >= 0) {
                        long phanNguyen = amount.longValue() / Constants.MENH_GIA_THE.HAI_CHUC.longValue();
                        DemandDtl dtl = new DemandDtl();
                        dtl.setDemand(demand);
                        dtl.setPrice(Constants.MENH_GIA_THE.HAI_CHUC);
                        dtl.setQuantity((int) phanNguyen);
                        demandDtls.add(dtl);
                        quantity20 += (int) phanNguyen;
                        amount = amount.subtract(Constants.MENH_GIA_THE.HAI_CHUC.multiply(BigDecimal.valueOf(phanNguyen)));
                    }

                    if (amount.compareTo(Constants.MENH_GIA_THE.MUOI_NGHIN) >= 0) {
                        long phanNguyen = amount.longValue() / Constants.MENH_GIA_THE.MUOI_NGHIN.longValue();
                        DemandDtl dtl = new DemandDtl();
                        dtl.setDemand(demand);
                        dtl.setPrice(Constants.MENH_GIA_THE.MUOI_NGHIN);
                        dtl.setQuantity((int) phanNguyen);
                        demandDtls.add(dtl);
                        quantity10 += (int) phanNguyen;
                    }
                    demand.setBill(myBill);

                }
            }
            if (demandDtls.size() > 0) {
                demandDtlRepository.insertBulk(demandDtls);
            }

            // Sau khi lưu thông tin đơn hàng thành công vào đb của mình
            // chuyển đơn hàng sang bên cổng game
            List<DemandRequestDTO> demandRequestDTOS = new ArrayList<>();
            if (Boolean.TRUE.equals(myBill.getMoreFee())) {
                if (quantity10 > 0) {
                    demandRequestDTOS.add(new DemandRequestDTO(10000, quantity10));
                }
                if (quantity20 > 0) {
                    demandRequestDTOS.add(new DemandRequestDTO(20000, quantity20));
                }
                if (quantity50 > 0) {
                    demandRequestDTOS.add(new DemandRequestDTO(50000, quantity50));
                }
                if (quantity100 > 0) {
                    demandRequestDTOS.add(new DemandRequestDTO(100000, quantity100));
                }
                if (quantity200 > 0) {
                    demandRequestDTOS.add(new DemandRequestDTO(200000, quantity200));
                }
                if (quantity500 > 0) {
                    demandRequestDTOS.add(new DemandRequestDTO(500000, quantity500));
                }
                if (quantity1000 > 0) {
                    demandRequestDTOS.add(new DemandRequestDTO(1000000, quantity1000));
                }

            }
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                ResponseEntity<String> cbr;
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                if (!Boolean.TRUE.equals(myBill.getMoreFee())) {
                    BillRequestDTOType2 billRequestDTO = new BillRequestDTOType2();
                    billRequestDTO.setType(2);
                    billRequestDTO.setIssuer(myBill.getType().name());
                    billRequestDTO.setTranID(myBill.getId());
                    String signa = "2" + myBill.getType().name() + billRequestDTO.getClientIP() + Constants.PRIVATE_KEY;
                    String md5 = Common.createMd5(signa);
                    billRequestDTO.setSignature(md5);
                    billRequestDTO.setData(myBill.getAmount());
                    String billRequestJson = objectMapper.writeValueAsString(billRequestDTO);
                    HttpEntity<String> request = new HttpEntity<>(billRequestJson, headers);
                    cbr = restTemplate.postForEntity(Constants.GameUrl.CREATE_BILL_2, request, String.class);
                } else {
                    BillRequestDTOType1 billRequestDTOType1 = new BillRequestDTOType1();
                    billRequestDTOType1.setType(1);
                    billRequestDTOType1.setTranID(myBill.getId());
                    billRequestDTOType1.setIssuer(myBill.getType().name());
                    billRequestDTOType1.setData(demandRequestDTOS);
                    String signa = "1" + myBill.getType().name() + billRequestDTOType1.getClientIP() + Constants.PRIVATE_KEY;
                    String md5 = Common.createMd5(signa);
                    billRequestDTOType1.setSignature(md5);

                    String billRequestJson = objectMapper.writeValueAsString(billRequestDTOType1);
                    HttpEntity<String> request = new HttpEntity<>(billRequestJson, headers);
                    cbr = restTemplate.postForEntity(Constants.GameUrl.CREATE_BILL_1, request, String.class);
                }

                // Lấy dữ liệu về, ép kiểu về object và set status cho bull
                CreateBillResponse createBillResponse = objectMapper.readValue(cbr.getBody(), CreateBillResponse.class);
                if (createBillResponse.getErrorCode() == 1) {
                    myBill.setTranStatus(createBillResponse.getErrorCode());
                    myBill.setPartnerId(createBillResponse.getPartnerID());
                } else {
                    myBill.setStatus(0);
                    myBill.setPartnerId(0L);
                }

                billRepository.save(myBill);
            } catch (Exception e) {
                myBill.setStatus(0);
                billRepository.save(myBill);
                e.printStackTrace();
            }
            if (!Common.isAdmin(user)) {
                int dayOfMonth = LocalDate.now().getDayOfMonth();
                com.hieugie.banthe.domain.System system = systemRepository.findByStatusTrue().get();
                int userPercent;
                if (dayOfMonth >= system.getHighFreeFrom() && dayOfMonth <= system.getHignFeeTo()) {
                    if (myBill.getChargeType() == 1) {
                        userPercent = user.getFeePercentLv1();
                    } else {
                        userPercent = user.getFeePercentLv2();
                    }
                } else {
                    if (myBill.getChargeType() == 1) {
                        userPercent = user.getFeePercentLv1b();
                    } else {
                        userPercent = user.getFeePercentLv2b();
                    }
                }
                user.setAvailableAmount(user.getAvailableAmount().subtract(BigDecimal.valueOf(sum * userPercent / 100)));
                userRepository.save(user);
            }
            userService.clearUserCaches(user);
            return myBill;
        }
        throw new BadRequestAlertException("Het phien lam viec", "bill", "login");
    }

    /**
     * Get all the bills.
     *
     * @param pageable the pagination information
     * @param partnerId
     * @param fullName
     * @param status
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<MyBillDTO> findAll(Pageable pageable, String account, String partnerId, String fullName, Integer status) {
        log.debug("Request to get all Bills");
        Optional<User> userOptional = userService.getUserWithAuthorities();
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            AtomicBoolean isAdmin = new AtomicBoolean(false);
            user.getAuthorities().forEach(authority -> {
                if (authority.getName().equalsIgnoreCase(AuthoritiesConstants.ADMIN)) {
                    isAdmin.set(true);
                }
            });
            return billRepository.findByUserId(pageable, account, isAdmin.get() ? null : userOptional.get().getId(), partnerId, fullName, status);
        }
        return Page.empty();
    }


    /**
     * Get one bill by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public MyBillDTO findOne(Long id) {
        log.debug("Request to get Bill : {}", id);
        return billRepository.findOneById(id);
    }

    /**
     * Delete the bill by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        try {
            log.debug("Request to delete Bill : {}", id);
            // khi khách hàng hủy đơn hàng, cộng lại số tiền khả dụng cho khách hàng
            Optional<User> userWithAuthorities = userService.getUserWithAuthorities();
            if (!userWithAuthorities.isPresent()) {
                throw new BadRequestAlertException("Het phien lam viec", "bill", "login");
            }
            Optional<Bill> billOptional = billRepository.findById(id);
            if (!billOptional.isPresent()) {
                throw new BadRequestAlertException("Đơn hàng không tồn tại", "bill", "invalidBill");
            }
            if (billOptional.get().getStatus() == 0) {
                return;
            }

            // gọi sang bên kia để hủy đơn trước, nếu succes thì mới là hủy đơn
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            ObjectMapper objectMapper = new ObjectMapper();
            CancelBillDTO cancelBillDTO = new CancelBillDTO();
            cancelBillDTO.setPartnerID(billOptional.get().getPartnerId());
            String signa = billOptional.get().getPartnerId() + Constants.PRIVATE_KEY;
            String md5 = Common.createMd5(signa);

            cancelBillDTO.setSignature(md5);
            String cancelBillJson = objectMapper.writeValueAsString(cancelBillDTO);
            HttpEntity<String> request = new HttpEntity<>(cancelBillJson, headers);
            ResponseEntity<String> cbr = restTemplate.postForEntity(Constants.GameUrl.CANCEL_BILL, request, String.class);
            CreateBillResponse createBillResponse = objectMapper.readValue(cbr.getBody(), CreateBillResponse.class);
            java.lang.System.out.println(createBillResponse);
            if (createBillResponse.getErrorCode() == 1 || createBillResponse.getErrorCode() == -6) {
                User user = billOptional.get().getUser();
                user.setAvailableAmount(user.getAvailableAmount().add(billOptional.get().getAmount()).subtract(billOptional.get().getChargedAmount()));
                userRepository.save(user);
                userService.clearUserCaches(user);
                billRepository.updateStatus(id);
                return;
            }
            log.error(createBillResponse.toString());
            throw new BadRequestAlertException("loi huy the 2", "bill", "cancelBill");
        } catch (Exception ex) {
            for (int i = 0; i < ex.getStackTrace().length; i++) {
                log.error(ex.getStackTrace()[i].toString());
            }
            log.error(Arrays.toString(ex.getStackTrace()));
            log.error(ex.getMessage());
            throw new BadRequestAlertException("loi huy the", "bill", "cancelBill");
        }
    }

    @Override
    public BillDTO findById(Long id) {
        Optional<Bill> bill = billRepository.findById(id);
        if (bill.isPresent()) {
            List<Demand> demands = demandRepository.findByBillId(bill.get().getId());
            return new BillDTO(bill.get(), demands);
        }
        return new BillDTO();
    }

    @Override
    public BillDTO findById2(Long id) {
        Optional<Bill> bill = billRepository.findById(id);
        if (bill.isPresent()) {
            List<DemandCharge> demands = demandChargeRepository.findByPartnerId(bill.get().getPartnerId());
            return new BillDTO(demands, bill.get());
        }
        return new BillDTO();
    }

    @Override
    public List<SheetDTO> readSheetName(MultipartFile file) {
        List<SheetDTO> sheetDTOS = new ArrayList<>();
        try {
            Workbook workbook = createWorkbook(file);
            Iterator<Sheet> sheetIterator = workbook.sheetIterator();
            long i = 0;
            while (sheetIterator.hasNext()) {
                Sheet sheet = sheetIterator.next();
                sheetDTOS.add(new SheetDTO(i++, sheet.getSheetName()));
            }
        } catch (IOException e) {

            throw new BadRequestAlertException("file khong dung dinh dang", "excel", "invalidFormat");
        }
        return sheetDTOS;
    }

    @Override
    public BillDTO readSheet(MultipartFile file, Integer sheetNumber) {
        BillDTO billDTO = new BillDTO();
        List<Demand> demands = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;
        try {
            Workbook workbook = createWorkbook(file);

            Sheet sheet = workbook.getSheetAt(sheetNumber);
            if (!ExcelUtils.checkExcelHeader(sheet.getRow(0))) {
                throw new BadRequestAlertException("file khong dung dinh dang", "excel", "invalidFormat");
            }
            for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {
                try {
                    Row row = sheet.getRow(i);
                    if (row == null) {
                        continue;
                    }
                    Demand demand = new Demand();
                    String name = ExcelUtils.getCellData(row.getCell(0));
                    String account = ExcelUtils.getCellData(row.getCell(1));
                    String amount = ExcelUtils.getCellData(row.getCell(2));
                    String serviceType = ExcelUtils.getCellData(row.getCell(3));
                    if (Strings.isNullOrEmpty(amount) || Strings.isNullOrEmpty(account) || Strings.isNullOrEmpty(serviceType)) {
                        continue;
                    }
                    int serviceTypeInt = (int) Float.parseFloat(serviceType);
                    if (serviceTypeInt == 1 && !StringUtils.isNumeric(account)) {
                        continue;
                    } else if (serviceTypeInt == 4 && StringUtils.isNumeric(account)) {
                        continue;
                    }
                    BigDecimal amountB = (new BigDecimal(amount)).multiply(BigDecimal.valueOf(1000));
                    demand.setAccount(account);
                    demand.setAmount(amountB);
                    demand.setName(name);
                    demand.setServiceType(serviceTypeInt);
                    demands.add(demand);
                    totalAmount = totalAmount.add(amountB);
                } catch (Exception ignored) {

                }
            }
            billDTO.setDemands(demands);
            Bill bill = new Bill();
            bill.setAmount(totalAmount);
            billDTO.setBill(bill);
        } catch (Exception e) {
            throw new BadRequestAlertException("file khong dung dinh dang", "excel", "invalidFormat");
        }
        return billDTO;
    }

    private Workbook createWorkbook(MultipartFile file) throws IOException {
        Workbook workbook;
        String lowerCaseFileName = Objects.requireNonNull(file.getOriginalFilename()).toLowerCase();
        if (lowerCaseFileName.endsWith(".xlsx")) {
            workbook = new XSSFWorkbook(file.getInputStream());
        } else {
            workbook = new HSSFWorkbook(file.getInputStream());
        }
        return workbook;
    }
}
