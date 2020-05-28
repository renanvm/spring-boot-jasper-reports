package br.com.renan.jrtests.controller;


import br.com.renan.jrtests.service.ReportService;
import net.sf.jasperreports.engine.*;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
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
        parameters.put("minSalary", 15000.0);
        parameters.put("condition", " LAST_NAME ='Smith' ORDER BY FIRST_NAME");
        ByteArrayResource byteArrayResource = reportService.exportReportToPDF(reportService.getInputStream(), parameters);
        return new ResponseEntity<>(byteArrayResource, headers, HttpStatus.OK);
    }

    @GetMapping("/xls")
    public ResponseEntity<ByteArrayResource> printXls() throws JRException, IOException, SQLException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.set("Content-Disposition", "attachment; filename=test.xls");
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("title", "Employee Report");
        parameters.put("minSalary", 15000.0);
        parameters.put("condition", " LAST_NAME ='Smith' ORDER BY FIRST_NAME");
        ByteArrayResource byteArrayResource = reportService.exportReportToXLS(reportService.getInputStream(), parameters);
        return new ResponseEntity<>(byteArrayResource, headers, HttpStatus.OK);
    }

}


    /*@PostMapping(value = "/pdf")
    public ResponseEntity<String> imprimir(@RequestParam Map<String, Object> parametros, HttpServletResponse response) throws JRException, SQLException, IOException, SQLException {

        parametros = parametros == null ? parametros = new HashMap<>() : parametros;

        // Pega o arquivo .jasper localizado em resources
        InputStream jasperStream = this.getClass().getResourceAsStream("/report/employeeReport.jasper");

        // Cria o objeto JaperReport com o Stream do arquivo jasper
        JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperStream);
        // Passa para o JasperPrint o relatório, os parâmetros e a fonte dos dados, no caso uma conexão ao banco de dados
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, dataSource.getConnection());

        // Configura a respota para o tipo PDF
        response.setContentType("application/pdf");
        // Define que o arquivo pode ser visualizado no navegador e também nome final do arquivo
        // para fazer download do relatório troque 'inline' por 'attachment'
        response.setHeader("Content-Disposition", "inline; filename=livros.pdf");

        // Faz a exportação do relatório para o HttpServletResponse
        final OutputStream outStream = response.getOutputStream();
        JasperExportManager.exportReportToPdfStream(jasperPrint, outStream);

        return new ResponseEntity<>("Customer has been create!", HttpStatus.OK);
    }*/
