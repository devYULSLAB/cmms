package com.cmms.common.excel;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.List;

@Service
public class ExcelService {

    /**
     * Generic method to create an Excel workbook from a list of objects.
     * @param data The list of data objects.
     * @param headers The column headers for the Excel file.
     * @param fieldNames The names of the fields to extract from the data objects, in order.
     * @return An Apache POI Workbook object.
     * @param <T> The type of the data objects.
     */
    public <T> Workbook createWorkbook(List<T> data, String[] headers, String[] fieldNames) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Data");

        // Create Header Row
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            headerRow.createCell(i).setCellValue(headers[i]);
        }

        // Create Data Rows
        int rowNum = 1;
        for (T item : data) {
            Row row = sheet.createRow(rowNum++);
            for (int i = 0; i < fieldNames.length; i++) {
                try {
                    Field field = item.getClass().getDeclaredField(fieldNames[i]);
                    field.setAccessible(true);
                    Object value = field.get(item);
                    Cell cell = row.createCell(i);
                    if (value != null) {
                        cell.setCellValue(value.toString());
                    } else {
                        cell.setCellValue("");
                    }
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    // In case of error, put a placeholder in the cell
                    row.createCell(i).setCellValue("N/A");
                }
            }
        }

        // Auto-size columns
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        return workbook;
    }
}
