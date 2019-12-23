package com.example.demo.models.utils;

import com.example.demo.models.MulticriteriaTable;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class ExcelReader {
    private File file;

    public ExcelReader(File file) {
        this.file = file;
    }

    public List<MulticriteriaTable> saveFileContent() {
        Workbook workbook;
        try {
            workbook = WorkbookFactory.create(file);
        } catch (IOException | InvalidFormatException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
        Sheet sheet = workbook.getSheetAt(0);
        Iterator<Row> rowIterator = sheet.rowIterator();

        DataFormatter dataFormatter = new DataFormatter();

        List<MulticriteriaTable> result = new ArrayList<>();
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();

            // To bypass Title row
            if (row.getRowNum() == 0)
                continue;

            Iterator<Cell> cellIterator = row.cellIterator();
            List<Double> rowValues = new ArrayList<>();
            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();
                String cellValue = dataFormatter.formatCellValue(cell);
                cellValue = cellValue.replace(',', '.');
                rowValues.add(Double.parseDouble(cellValue));
            }
            MulticriteriaTable tableObject = new MulticriteriaTable();
            tableObject.setCcMin(rowValues.get(0).intValue());
            tableObject.setCcMax(rowValues.get(1).intValue());
            tableObject.setCarAgeMin(rowValues.get(2).intValue());
            tableObject.setCarAgeMax(rowValues.get(3).intValue());
            tableObject.setBaseAmount(rowValues.get(4));
            result.add(tableObject);
        }
        return result;
    }
}
