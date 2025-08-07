package com.project.config;

import java.io.FileInputStream;
import java.util.HashMap;
import com.project.base.DriverFactory;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtilities extends DriverFactory {

    private static FileInputStream DataFile;
    private static XSSFSheet ExcelWSheet;
    private static XSSFWorkbook ExcelWBook;
    private static XSSFRow ColHeader;
    private static XSSFRow data;

    /**
     * This method is to set excel sheet.
     **/
    public static void setExcelSheet(String FileName, String SheetName) throws Exception {
        try {
            String PathToDataFile = System.getProperty("user.dir") + "\\src\\test\\resources\\WebAutomation\\TestData\\" + FileName + ".xlsx";
            FileInputStream DataFile = new FileInputStream(PathToDataFile);
            ExcelWBook = new XSSFWorkbook(DataFile);
            ExcelWSheet = ExcelWBook.getSheet(SheetName);
        } catch (Exception e) {
            e.printStackTrace();
            throw (e);
        }
    }

    /**
     * This method is to get cell data.
     **/
    public static HashMap<String, String> getCellData(int rowNum) {
        try {
            HashMap<String, String> edata = new HashMap<String, String>();
            DataFormatter formatter = new DataFormatter();
            ColHeader = ExcelWSheet.getRow(0);
            data = ExcelWSheet.getRow(rowNum);
            String header, data;

            for (int i = 0; i < ColHeader.getLastCellNum(); i++) {
                header = formatter.formatCellValue(ExcelWSheet.getRow(0).getCell(i));
                data = formatter.formatCellValue(ExcelWSheet.getRow(rowNum).getCell(i));
                edata.put(header, data);
            }
            return edata;
        } catch (Exception e) {
            e.printStackTrace();
            throw (e);
        }
    }

    /**
     * This method is to get cell data.
     **/
    public static HashMap<String, String> getCellData(int headerRowNum, int rowNum) {
        try {
            HashMap<String, String> edata = new HashMap<String, String>();
            DataFormatter formatter = new DataFormatter();
            ColHeader = ExcelWSheet.getRow(headerRowNum);
            data = ExcelWSheet.getRow(rowNum);
            String header, data;

            for (int i = 0; i < ColHeader.getLastCellNum(); i++) {
                header = formatter.formatCellValue(ExcelWSheet.getRow(headerRowNum).getCell(i));
                data = formatter.formatCellValue(ExcelWSheet.getRow(rowNum).getCell(i));
                edata.put(header, data);
            }
            return edata;
        } catch (Exception e) {
            e.printStackTrace();
            throw (e);
        }
    }

    /**
     * This method is to get all elements.
     **/
    public static HashMap<String, String> getAllElements(int coNum) {
        try {
            HashMap<String, String> edata = new HashMap<String, String>();
            int numOfRows = ExcelWSheet.getLastRowNum();
            DataFormatter formatter = new DataFormatter();
            String key, val;

            for (int i = 1; i <= numOfRows; i++) {
                key = formatter.formatCellValue(ExcelWSheet.getRow(i).getCell(0));
                val = formatter.formatCellValue(ExcelWSheet.getRow(i).getCell(coNum));
                edata.put(key, val);
            }
            return edata;
        } catch (Exception e) {
            e.printStackTrace();
            throw (e);
        }
    }

    /**
     * This method is to close excel.
     **/
    public static void closeExcel() throws Exception {
        ExcelWBook.close();
    }
}
