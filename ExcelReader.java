package org.example;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.DataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;

@Service
public class ExcelReader {

    private final DataSource dataSource;

    ExcelReader(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void ExcelReadInsert(MultipartFile file) throws Exception {

        try (Connection conn = dataSource.getConnection();
             XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream())) {

            XSSFSheet sheet = workbook.getSheetAt(0);
            String sql = "INSERT INTO \"companyPlates\" (\"plateNumber\") VALUES (?) ON CONFLICT (\"plateNumber\") DO NOTHING;";
            try (PreparedStatement pm = conn.prepareStatement(sql)) {

                int rowNum = 1; // 0 başlık satırı
                XSSFRow row = sheet.getRow(rowNum);

                while (row != null && row.getCell(0) != null) {
                    String celldata = row.getCell(0).getStringCellValue();
                    pm.setString(1, celldata);
                    pm.addBatch();
                    System.out.println("Eklenecek: " + celldata);
                    rowNum++;
                    row = sheet.getRow(rowNum);
                }

                pm.executeBatch();
            }
        }
    }
}