package com.hieugie.banthe.service.util;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;

import java.util.Arrays;
import java.util.List;

public class ExcelUtils {
    static List<String> header = Arrays.asList("TEN_NGUOI_NAP", "TAI_KHOAN", "SO_TIEN");

    public static boolean checkExcelHeader(Row row0) {
        try {
            if (!row0.getCell(0).getStringCellValue().equalsIgnoreCase(header.get(0))
                || !row0.getCell(1).getStringCellValue().equalsIgnoreCase(header.get(1))
                || !row0.getCell(2).getStringCellValue().equalsIgnoreCase(header.get(2))) {
                return false;
            }
        } catch (Exception ex) {
            return false;
        }
        return true;
    }

    public static String getCellData(Cell cell) {
        if (cell.getCellTypeEnum().equals(CellType.STRING)) {
            return cell.getStringCellValue();
        } else if (cell.getCellTypeEnum().equals(CellType.NUMERIC)) {
            return String.valueOf(cell.getNumericCellValue());
        } else if (cell.getCellTypeEnum().equals(CellType.FORMULA)) {
            return String.valueOf(cell.getCellFormula());
        }
        return null;
    }
}
