package org.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/excel")
public class TakeExcel {

    private ExcelReader excelService;

    @Autowired
    public TakeExcel(ExcelReader excelService) {
        this.excelService = excelService;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadExcel(@RequestParam("excelfile") MultipartFile file) {
        try {
            excelService.ExcelReadInsert(file);
            return ResponseEntity.ok("Excele yüklendi");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hata: " + e.getMessage());
        }
    }
}
