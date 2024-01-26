package org.jaspereport.builders.impl;

import net.sf.dynamicreports.report.builder.FieldBuilder;
import net.sf.dynamicreports.report.exception.DRException;
import net.sf.jasperreports.engine.JRDataSource;
import org.jaspereport.builders.ReportBuilder;

import static net.sf.dynamicreports.report.builder.DynamicReports.type;
import static net.sf.dynamicreports.report.builder.DynamicReports.cht;
import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;
import static net.sf.dynamicreports.report.builder.DynamicReports.field;
import static net.sf.dynamicreports.report.builder.DynamicReports.report;
import static net.sf.dynamicreports.report.builder.DynamicReports.stl;

public class BarChartBuilder implements ReportBuilder {
    private static final String TITLE = "Holidays";
    private static final String CHART_TITLE = "Bar chart";
    private static final String MONTH_FIELD_NAME = "Month";
    private static final String COUNTRY_FIELD_NAME = "Country";
    private static final String QUANTITY_FIELD_NAME = "Quantity";

    public void buildReport(JRDataSource jrDataSource) {
        FieldBuilder<String> month = field(MONTH_FIELD_NAME, type.stringType());
        FieldBuilder<String> country = field(COUNTRY_FIELD_NAME, type.stringType());
        FieldBuilder<Integer> quantity = field(QUANTITY_FIELD_NAME, type.integerType());

        try {
            report()
                    .title(cmp.text(TITLE))
                    .summary(
                            cht.barChart()
                                    .setTitle(CHART_TITLE)
                                    .setTitleFont(stl.font().bold())
                                    .setCategory(month)
                                    .series(
                                            cht.serie(quantity).setSeries(country)
                                    )
                    )
                    .setDataSource(jrDataSource)
                    .pageFooter(cmp.pageNumber())
                    .show();
        } catch (DRException e) {
            throw new RuntimeException("Error occurred during report creation " + e);
        }
    }
}
