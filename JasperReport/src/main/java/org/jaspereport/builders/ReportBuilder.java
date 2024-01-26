package org.jaspereport.builders;

import net.sf.jasperreports.engine.JRDataSource;

public interface ReportBuilder {
    void buildReport(JRDataSource jrDataSource);
}
