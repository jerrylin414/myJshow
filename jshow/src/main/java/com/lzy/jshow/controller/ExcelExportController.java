package com.lzy.jshow.controller;


import com.lzy.jshow.serivce.impl.ExportServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@RequestMapping("/api/excel")
@RestController
public class ExcelExportController {
    @Autowired
    private ExportServiceImpl exportService;

    @GetMapping("/export1")
    public ResponseEntity<byte[]> export1(){
        ByteArrayOutputStream outputStream = exportService.export1();
        return ResponseEntity.ok()
                .cacheControl(CacheControl.noCache())
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + URLEncoder.encode("fill_excel.xlsx", StandardCharsets.UTF_8) + "\"")
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(outputStream.toByteArray());

    }

    @GetMapping("/export2")
    public ResponseEntity<byte[]> export2(){
        ByteArrayOutputStream outputStream = exportService.export2();
        return ResponseEntity.ok()
                .cacheControl(CacheControl.noCache())
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + URLEncoder.encode("gen_fill_excel.xlsx", StandardCharsets.UTF_8) + "\"")
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(outputStream.toByteArray());

    }

}
