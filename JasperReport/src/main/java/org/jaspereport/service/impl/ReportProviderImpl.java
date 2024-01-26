package org.jaspereport.service.impl;

import java.util.List;
import java.util.Scanner;

import org.jaspereport.builders.impl.BarChartBuilder;
import org.jaspereport.builders.impl.CrosstabChartCombinedReport;
import org.jaspereport.builders.impl.CrosstabReportBuilder;
import org.jaspereport.builders.impl.DynamicReportBuilder;
import org.jaspereport.builders.impl.JasperTemplateReportBuilder;
import org.jaspereport.domain.Holiday;
import org.jaspereport.service.ReportProvider;

public class ReportProviderImpl implements ReportProvider {
    private static final XmlParserImpl parser = new XmlParserImpl();
    private static final DataSourceProviderImpl DATA_SOURCE_PROVIDER_IMPL
            = new DataSourceProviderImpl();
    private static final JasperTemplateReportBuilder jasperTemplateReportBuilder
            = new JasperTemplateReportBuilder();
    private static final DynamicReportBuilder dynamicBuilder
            = new DynamicReportBuilder();
    private static final CrosstabReportBuilder crosstabBuilder
            = new CrosstabReportBuilder();
    private static final BarChartBuilder chartBuilder
            = new BarChartBuilder();
    private static final CrosstabChartCombinedReport combinedReport
            = new CrosstabChartCombinedReport();
    public static final String FILE_NAME = "src/main/resources/MyDataBase.xml";

    public void viewReport() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Available report types:");
        System.out.println("1. JasperTemplate.");
        System.out.println("2. Standard Dynamic Report (JrMapDataSource).");
        System.out.println("3. Standard Dynamic Report (JrBeanDataSource).");
        System.out.println("4. Standard Dynamic Report (JrResultSetDatasource).");
        System.out.println("5. Crosstab.");
        System.out.println("6. Bar Chart.");
        System.out.println("7. Combined Crosstab Bar Chart.");
        System.out.print("Choose report type: ");

        List<Holiday> holidays = parser.parseXml(FILE_NAME);

        if (scanner.hasNextInt()) {
            switch (scanner.nextInt()) {
                case 1:
                    jasperTemplateReportBuilder.buildReport(DATA_SOURCE_PROVIDER_IMPL.getJrMapDataSource(holidays));
                    break;
                case 2:
                    dynamicBuilder.buildReport(DATA_SOURCE_PROVIDER_IMPL.getJrMapDataSource(holidays));
                    break;
                case 3:
                    dynamicBuilder.buildReport(DATA_SOURCE_PROVIDER_IMPL.getJrBeanDataSource(holidays));
                    break;
                case 4:
                    dynamicBuilder.buildReport(DATA_SOURCE_PROVIDER_IMPL.getJrResultSetDataSource(holidays));
                    break;
                case 5:
                    crosstabBuilder.buildReport(DATA_SOURCE_PROVIDER_IMPL.getJrBeanDataSourceCrosstabChart(holidays));
                    break;
                case 6:
                    chartBuilder.buildReport(DATA_SOURCE_PROVIDER_IMPL.getJrBeanDataSourceCrosstabChart(holidays));
                    break;
                case 7:
                    combinedReport.buildReport(DATA_SOURCE_PROVIDER_IMPL.getJrBeanDataSourceCrosstabChart(holidays));
                    break;
                default:
                    break;
            }
        }
        scanner.close();
    }
}
