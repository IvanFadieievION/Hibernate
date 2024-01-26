package org.jaspereport.builders.impl;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.JasperViewer;
import org.jaspereport.builders.ReportBuilder;

public class JasperTemplateReportBuilder implements ReportBuilder {
    public static final String OUTPUT_FILE_NAME
            = "src/main/resources/JasperTemplates/JasperTemplate.jrxml";
    public static final String JRXML_FILE_NAME
            = "C:/Users/crme123/JaspersoftWorkspace/MyReports/Output";

    public void buildReport(JRDataSource dataSource){
        try {
            JasperCompileManager.compileReportToFile(JRXML_FILE_NAME, OUTPUT_FILE_NAME);
            JasperPrint jasperPrint = JasperFillManager.fillReport(OUTPUT_FILE_NAME, null, dataSource);
            JasperViewer.viewReport(jasperPrint);
        } catch (JRException e) {
            throw new RuntimeException("Error occurred during report creating.");
        }
    }
}
