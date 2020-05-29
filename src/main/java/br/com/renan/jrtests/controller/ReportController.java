package br.com.renan.jrtests.controller;


import br.com.renan.jrtests.service.ReportService;
import net.sf.jasperreports.engine.*;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.util.MimeType;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/report")
public class ReportController {

    private ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/pdf")
    public ResponseEntity<ByteArrayResource> printPdf() throws JRException, IOException, SQLException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.set("Content-Disposition", "attachment; filename=test.pdf");
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("title", "Employee Report");
        ByteArrayResource byteArrayResource = reportService.exportReportToPDF(reportService.getInputStream(), parameters, reportService.getEmployees());
        return new ResponseEntity<>(byteArrayResource, headers, HttpStatus.OK);
    }

    @GetMapping("/xls")
    public ResponseEntity<ByteArrayResource> printXls() throws JRException, IOException, SQLException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.asMediaType(MimeType.valueOf("application/vnd.ms-excel")));
        headers.set("Content-Disposition", "attachment; filename=test.xls");
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("title", "Employee Report");
        ByteArrayResource byteArrayResource = reportService.exportReportToXLS(reportService.getInputStream(), parameters, reportService.getEmployees());
        return new ResponseEntity<>(byteArrayResource, headers, HttpStatus.OK);
    }

    @GetMapping("/csv")
    public ResponseEntity<ByteArrayResource> printCsv() throws JRException, IOException, SQLException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.asMediaType(MimeType.valueOf("application/vnd.ms-excel")));
        headers.set("Content-Disposition", "attachment; filename=test.csv");
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("title", "Employee Report");
        ByteArrayResource byteArrayResource = reportService.exportReportToCSV(reportService.getInputStream(), parameters, reportService.getEmployees());
        return new ResponseEntity<>(byteArrayResource, headers, HttpStatus.OK);
    }

}