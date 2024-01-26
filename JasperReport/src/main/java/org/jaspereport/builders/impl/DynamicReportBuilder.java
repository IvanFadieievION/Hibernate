package org.jaspereport.builders.impl;

import net.sf.dynamicreports.report.builder.component.HorizontalListBuilder;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.constant.HorizontalImageAlignment;
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment;
import net.sf.dynamicreports.report.exception.DRException;
import net.sf.jasperreports.engine.JRDataSource;
import org.jaspereport.builders.ReportBuilder;

import java.awt.Color;

import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;
import static net.sf.dynamicreports.report.builder.DynamicReports.col;
import static net.sf.dynamicreports.report.builder.DynamicReports.margin;
import static net.sf.dynamicreports.report.builder.DynamicReports.report;
import static net.sf.dynamicreports.report.builder.DynamicReports.type;
import static net.sf.dynamicreports.report.builder.DynamicReports.stl;

public class DynamicReportBuilder implements ReportBuilder {
    private static final String LIGHT_BLUE = "#91BDEB";
    private static final String DARK_BLUE = "#006699";
    private static final String LIGHT_GREY = "#E6E6E6";
    public static final int TITLE_FONT_SIZE = 34;
    public static final int COLUMN_FONT_SIZE = 14;
    public static final int PADDING = 10;
    public static final int TOP_PADDING = 3;
    public static final int BOTTOM_PADDING = 3;
    public static final String DATE_PATTERN = "EEEEE dd MMMMM";
    public static final String COUNTRY = "Country";
    public static final String DATA_SOURCE_COUNTY_FIELD = "COUNTRY";
    public static final String NAME = "Name";
    public static final String DATA_SOURCE_NAME_FIELD = "NAME";
    public static final String DATE = "Date";
    public static final String DATA_SOURCE_DATE_FIELD = "DATE";
    public static final int VERTICAL_GAP_HEIGHT = 10;
    public static final String HOLIDAYS = "Holidays";
    public static final String ION_LOGO_PNG = "src/main/resources/ionlogo.png";
    public static final int TEXT_HEIGHT = 70;
    public static final int BOTTOM_MARGIN = 20;

    public void buildReport(JRDataSource dataSource) {

        StyleBuilder titleTextStyle = stl.style()
                .setFontSize(TITLE_FONT_SIZE)
                .setForegroundColor(Color.WHITE)
                .bold()
                .setPadding(PADDING);

        StyleBuilder imageStyle = stl.style()
                .setPadding(PADDING)
                .setHorizontalImageAlignment(HorizontalImageAlignment.RIGHT);

        StyleBuilder titleBackground = stl.style()
                .setBackgroundColor(Color.decode(DARK_BLUE));

        StyleBuilder columnsHeaderStyle = stl.style()
                .bold()
                .setLeftPadding(PADDING)
                .setTopPadding(TOP_PADDING)
                .setBottomPadding(BOTTOM_PADDING)
                .setFontSize(COLUMN_FONT_SIZE)
                .setBackgroundColor(Color.decode(LIGHT_BLUE))
                .setBottomBorder(stl.pen1Point())
                .setTopBorder(stl.pen1Point());

        StyleBuilder columnContentStyle = stl.style()
                .setLeftPadding(PADDING)
                .setTopPadding(TOP_PADDING)
                .setBottomPadding(BOTTOM_PADDING)
                .setFontSize(COLUMN_FONT_SIZE)
                .setBottomBorder(stl.pen1Point());

        StyleBuilder pageFooterBackground = stl.style()
                .setLeftPadding(PADDING)
                .setBackgroundColor(Color.decode(LIGHT_GREY));

        HorizontalListBuilder pageFooterContent = cmp.horizontalList(
                cmp.currentDate().setPattern(DATE_PATTERN)
                        .setHorizontalTextAlignment(HorizontalTextAlignment.LEFT),
                cmp.pageXofY().setHorizontalTextAlignment(HorizontalTextAlignment.RIGHT));

        try {
            report().
                columns(
                    col.column(COUNTRY, DATA_SOURCE_COUNTY_FIELD, type.stringType())
                            .setStyle(columnContentStyle),
                    col.column(NAME, DATA_SOURCE_NAME_FIELD, type.stringType())
                            .setStyle(columnContentStyle),
                    col.column(DATE, DATA_SOURCE_DATE_FIELD, type.stringType())
                            .setStyle(columnContentStyle)
                ).setColumnTitleStyle(columnsHeaderStyle)
                .title(
                        cmp.verticalGap(VERTICAL_GAP_HEIGHT),
                        cmp.horizontalList(
                            cmp.text(HOLIDAYS).setStyle(titleTextStyle),
                            cmp.image(ION_LOGO_PNG)
                                    .setStyle(imageStyle)
                                    .setHeight(TEXT_HEIGHT)
                        ),
                        cmp.verticalGap(VERTICAL_GAP_HEIGHT)
                ).setTitleStyle(titleBackground)
                    .pageFooter(pageFooterContent)
                    .setPageFooterStyle(pageFooterBackground)
                .setPageMargin(margin().setBottom(BOTTOM_MARGIN))
                .setDataSource(dataSource)
                .show();
        } catch (DRException e) {
            throw new RuntimeException("Error occurred during report creation " + e);
        }
    }
}
