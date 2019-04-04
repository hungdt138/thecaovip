package com.hieugie.banthe.service;

import com.hieugie.banthe.domain.Bill;
import com.hieugie.banthe.web.rest.dto.BillDTO;
import com.hieugie.banthe.web.rest.dto.MyBillDTO;
import com.hieugie.banthe.web.rest.dto.SheetDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Service Interface for managing Bill.
 */
public interface BillService {

    /**
     * Save a bill.
     *
     * @param bill the entity to save
     * @return the persisted entity
     */
    Bill save(BillDTO bill);

    /**
     * Get all the bills.
     *
     * @param pageable the pagination information
     * @param partnerId
     * @param fullName
     * @param status
     * @return the list of entities
     */
    Page<MyBillDTO> findAll(Pageable pageable, String account, String partnerId, String fullName, Integer status);


    /**
     * Get the "id" bill.
     *
     * @param id the id of the entity
     * @return the entity
     */
    MyBillDTO findOne(Long id);

    /**
     * Delete the "id" bill.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    BillDTO findById(Long id);

    List<SheetDTO> readSheetName(MultipartFile file);

    BillDTO readSheet(MultipartFile file, Integer sheetNumber);

    BillDTO findById2(Long id);
}
