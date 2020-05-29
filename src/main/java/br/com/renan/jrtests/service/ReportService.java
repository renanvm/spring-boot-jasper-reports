package br.com.renan.jrtests.service;

import br.com.renan.jrtests.model.Employee;
import br.com.renan.jrtests.repository.EmployeeRepository;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRCsvExporter;
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
import java.util.List;
import java.util.Map;

@Service
public class ReportService {

    @Autowired
    DataSource dataSource;

    @Autowired
    private EmployeeRepository employeeRepository;

    public InputStream getInputStream() {
        InputStream employeeReportStream
                = getClass().getResourceAsStream("/relatorios/employeeReport.jrxml");
        return employeeReportStream;
    }

    public ByteArrayResource exportReportToPDF(InputStream targetStream, Map parameters, List dataList) throws SQLException, JRException, IOException {
        JasperReport jasperReport = JasperCompileManager.compileReport(targetStream);
        JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(dataList);
        //JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource.getConnection());
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, beanColDataSource);
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

    public ByteArrayResource exportReportToXLS(InputStream targetStream, Map parameters, List dataList) throws SQLException, JRException, IOException {
        JasperReport jasperReport = JasperCompileManager.compileReport(targetStream);
        JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(dataList);
       // JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource.getConnection());
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, beanColDataSource);
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

    public ByteArrayResource exportReportToCSV(InputStream targetStream, Map parameters, List dataList) throws SQLException, JRException, IOException {
        JasperReport jasperReport = JasperCompileManager.compileReport(targetStream);
        JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(dataList);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, beanColDataSource);
        //JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource.getConnection());
        JRCsvExporter exporter = new JRCsvExporter();
        ByteArrayOutputStream xlsReport = new ByteArrayOutputStream();
        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        File csv = File.createTempFile("output.", ".csv");
        exporter.setExporterOutput(new SimpleWriterExporterOutput(csv));
        exporter.exportReport();
        byte[] fileContent = Files.readAllBytes(csv.toPath());
        return new ByteArrayResource(fileContent);
    }

    public List<Employee> getEmployees(){
        return this.employeeRepository.findAll();
    }
}


