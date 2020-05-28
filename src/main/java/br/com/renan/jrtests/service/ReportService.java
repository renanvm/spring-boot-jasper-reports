package br.com.renan.jrtests.service;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.io.*;
import java.nio.file.Files;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@Service
public class ReportService {

    @Autowired
    DataSource dataSource;

    /*public void compileReport() {
        InputStream employeeReportStream
                = getClass().getResourceAsStream("/relatorios/employeeReport.jrxml");
        JasperReport jasperReport
                = null;
        try {
            jasperReport = JasperCompileManager.compileReport(employeeReportStream);
        } catch (JRException e) {
            e.printStackTrace();
        }
    }*/

    public InputStream getInputStream() {
        InputStream employeeReportStream
                = getClass().getResourceAsStream("/relatorios/employeeReport.jrxml");
        return employeeReportStream;
    }

    public ByteArrayResource exportReportToPDF(InputStream targetStream, Map parameters) throws SQLException, JRException, IOException {
        JasperReport jasperReport = JasperCompileManager.compileReport(targetStream);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource.getConnection());
        JRPdfExporter exporter = new JRPdfExporter();
        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        File pdf = File.createTempFile("output.", ".pdf");
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(pdf));
        SimplePdfReportConfiguration reportConfig
                = new SimplePdfReportConfiguration();
        reportConfig.setSizePageToContent(true);
        exporter.setConfiguration(reportConfig);
        exporter.exportReport();
        byte[] fileContent = Files.readAllBytes(pdf.toPath());
        return new ByteArrayResource(fileContent);
    }

    public ByteArrayResource exportReportToXLS(InputStream targetStream, Map parameters) throws SQLException, JRException, IOException {
        JasperReport jasperReport = JasperCompileManager.compileReport(targetStream);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource.getConnection());
        JRXlsxExporter exporter = new JRXlsxExporter();
        ByteArrayOutputStream xlsReport = new ByteArrayOutputStream();
        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        File xls = File.createTempFile("output.", ".xls");
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(xls));
        SimpleXlsxReportConfiguration reportConfig
                = new SimpleXlsxReportConfiguration();
        exporter.setConfiguration(reportConfig);
        exporter.exportReport();
        byte[] fileContent = Files.readAllBytes(xls.toPath());
        return new ByteArrayResource(fileContent);
    }
}


