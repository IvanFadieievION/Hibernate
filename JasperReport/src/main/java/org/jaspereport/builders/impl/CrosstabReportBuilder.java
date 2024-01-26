package org.jaspereport.builders.impl;

import net.sf.dynamicreports.report.builder.crosstab.CrosstabBuilder;
import net.sf.dynamicreports.report.builder.crosstab.CrosstabColumnGroupBuilder;
import net.sf.dynamicreports.report.builder.crosstab.CrosstabRowGroupBuilder;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.constant.Calculation;
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment;
import net.sf.dynamicreports.report.constant.PageOrientation;
import net.sf.dynamicreports.report.constant.PageType;
import net.sf.dynamicreports.report.exception.DRException;
import net.sf.jasperreports.engine.JRDataSource;
import org.jaspereport.builders.ReportBuilder;

import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;
import static net.sf.dynamicreports.report.builder.DynamicReports.ctab;
import static net.sf.dynamicreports.report.builder.DynamicReports.report;
import static net.sf.dynamicreports.report.builder.DynamicReports.stl;

public class CrosstabReportBuilder implements ReportBuilder {
    private static final String COUNTRY_COLUMN = "Country";
    private static final String MONTH_COLUMN = "Month";
    private static final String TOTAL_HEADER = "Total";
    private static final String HEADER_TEXT = "State / Mese";
    private static final String QUANTITY_MEASURE = "Quantity";
    public static final int HEADER_CELL_HEIGHT = 13;
    public static final int ROW_WIDTH = 70;

    public void buildReport(JRDataSource jrDataSource) {

        try {
            report().setPageFormat(PageType.A4, PageOrientation.LANDSCAPE)
                    .summary(getCrosstabBuilder())
                    .setSummaryStyle(stl.style()
                            .setHorizontalTextAlignment(HorizontalTextAlignment.CENTER))
                    .setDataSource(jrDataSource)
                    .show();
        } catch (DRException e) {
            throw new RuntimeException("Error occurred during report creation " + e);
        }
    }

    public CrosstabBuilder getCrosstabBuilder() {
        CrosstabRowGroupBuilder<String> rowGroup
                = ctab.rowGroup(COUNTRY_COLUMN, String.class)
                .setTotalHeader(TOTAL_HEADER)
                .setHeaderStyle(stl.style().setHorizontalTextAlignment(HorizontalTextAlignment.CENTER))
                .setTotalHeaderStyle(stl.style().setHorizontalTextAlignment(HorizontalTextAlignment.CENTER));

        CrosstabColumnGroupBuilder<String> columnGroup
                = ctab.columnGroup(MONTH_COLUMN, String.class);

        return ctab.crosstab()
                .headerCell(cmp.text(HEADER_TEXT)
                        .setHeight(HEADER_CELL_HEIGHT)
                        .setStyle(stl.style().setHorizontalTextAlignment(HorizontalTextAlignment.CENTER)))
                .rowGroups(rowGroup).setCellWidth(ROW_WIDTH)
                .setCellStyle(stl.style().setHorizontalTextAlignment(HorizontalTextAlignment.CENTER))
                .columnGroups(columnGroup)
                .setGrandTotalStyle(stl.style().setHorizontalTextAlignment(HorizontalTextAlignment.CENTER))
                .setGroupStyle(stl.style().setHorizontalTextAlignment(HorizontalTextAlignment.CENTER))
                .measures(
                        ctab.measure(QUANTITY_MEASURE, Integer.class, Calculation.COUNT)
                                .setStyle(stl.style().setHorizontalTextAlignment(HorizontalTextAlignment.CENTER))
                );
    }

    private StyleBuilder boldCenteredStyle() {
        return stl.style().setBorder(stl.pen1Point())
                .setHorizontalTextAlignment(HorizontalTextAlignment.CENTER);
    }
}
