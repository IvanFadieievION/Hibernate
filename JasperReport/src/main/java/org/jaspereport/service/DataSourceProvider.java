package org.jaspereport.service;

import net.sf.jasperreports.engine.JRDataSource;
import org.jaspereport.domain.Holiday;

import java.util.List;

public interface DataSourceProvider {
    JRDataSource getJrMapDataSource(List<Holiday> holidays);
    JRDataSource getJrResultSetDataSource(List<Holiday> holidays);
    JRDataSource getJrBeanDataSource(List<Holiday> holidays);
    JRDataSource getJrBeanDataSourceCrosstabChart(List<Holiday> holidays);
}
