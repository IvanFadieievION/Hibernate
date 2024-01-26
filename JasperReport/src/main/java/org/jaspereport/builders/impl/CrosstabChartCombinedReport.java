package org.jaspereport.builders.impl;

import net.sf.dynamicreports.report.builder.DynamicReports;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment;
import net.sf.dynamicreports.report.constant.PageOrientation;
import net.sf.dynamicreports.report.constant.PageType;
import net.sf.dynamicreports.report.exception.DRException;
import net.sf.jasperreports.engine.JRDataSource;
import org.jaspereport.builders.ReportBuilder;

import static net.sf.dynamicreports.report.builder.DynamicReports.type;
import static net.sf.dynamicreports.report.builder.DynamicReports.cht;
import static net.sf.dynamicreports.report.builder.DynamicReports.report;
import static net.sf.dynamicreports.report.builder.DynamicReports.stl;

public class CrosstabChartCombinedReport implements ReportBuilder {
    private static final String TITLE = "Bar chart";
    private static final String MONTH_COLUMN = "Month";
    private static final String COUNTRY_COLUMN = "Country";
    private static final String QUANTITY_COLUMN = "Quantity";

    public void buildReport (JRDataSource jrDataSource) {
        CrosstabReportBuilder crosstabReportBuilder = new CrosstabReportBuilder();
        TextColumnBuilder<String> month = DynamicReports.col.column(MONTH_COLUMN, type.stringType());
        TextColumnBuilder<String> country = DynamicReports.col.column(COUNTRY_COLUMN, type.stringType());
        TextColumnBuilder<Integer> quantity = DynamicReports.col.column(QUANTITY_COLUMN, type.integerType());

        try {
            report().setPageFormat(PageType.A4, PageOrientation.LANDSCAPE)
                    .summary(crosstabReportBuilder.getCrosstabBuilder(), cht.barChart()
                            .setTitle(TITLE)
                            .setTitleFont(stl.font().bold())
                            .setCategory(month)
                            .series(
                                    cht.serie(quantity).setSeries(country)
                            ))
                    .setSummaryStyle(stl.style()
                            .setHorizontalTextAlignment(HorizontalTextAlignment.CENTER))
                    .setDataSource(jrDataSource)
                    .sortBy(month)
                    .show();
        } catch (DRException e) {
            throw new RuntimeException("Error occurred during report creation " + e);
        }
    }
}
