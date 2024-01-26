package org.jaspereport.service.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import net.sf.dynamicreports.report.datasource.DRDataSource;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRResultSetDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;

import org.jaspereport.domain.Holiday;
import org.jaspereport.service.DataSourceProvider;

public class DataSourceProviderImpl implements DataSourceProvider {
    private static final SessionFactory sessionFactory
            = new Configuration().configure().buildSessionFactory();
    public static final String COUNTRY = "COUNTRY";
    public static final String NAME = "NAME";
    public static final String DATE = "DATE";
    public static final String DATA_SOURCE_MONTH_FIELD = "Month";
    public static final String DATA_SOURCE_COUNTRY_FIELD = "Country";
    public static final String DATA_SOURCE_QUANTITY_FIELD = "Quantity";
    public static final String SELECT_FROM_HOLIDAYS = "SELECT * FROM holidays";

    public JRDataSource getJrMapDataSource(List<Holiday> holidays) {
        List<Map<String, ?>> map = new ArrayList<>();
        for (Holiday holiday : holidays) {
            Map<String, String> holidayDetails = new HashMap<>();
            holidayDetails.put(COUNTRY, holiday.getCOUNTRY());
            holidayDetails.put(NAME, holiday.getNAME());
            holidayDetails.put(DATE, holiday.getDATE());
            map.add(holidayDetails);
        }
        return new JRMapCollectionDataSource(map);
    }

    public JRDataSource getJrResultSetDataSource(List<Holiday> holidays) {
        saveHolidaysToDB(holidays);
        return new JRResultSetDataSource(getResultSetFromDB());
    }

    public JRDataSource getJrBeanDataSource(List<Holiday> holidays) {
        return new JRBeanCollectionDataSource(holidays);
    }

    public JRDataSource getJrBeanDataSourceCrosstabChart(List<Holiday> holidays) {
        DRDataSource dataSource = new DRDataSource(DATA_SOURCE_MONTH_FIELD,
                DATA_SOURCE_COUNTRY_FIELD, DATA_SOURCE_QUANTITY_FIELD);
        for (Holiday holiday : holidays) {
            dataSource.add(holiday.getMONTH(), holiday.getCOUNTRY(), 1);
        }
        return dataSource;
    }

    private ResultSet getResultSetFromDB() {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            Connection connection = sessionFactory.
                    getSessionFactoryOptions().getServiceRegistry().
                    getService(ConnectionProvider.class).getConnection();
            PreparedStatement preparedStatement
                    = connection.prepareStatement(SELECT_FROM_HOLIDAYS);
            return preparedStatement.executeQuery();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("Error occurred during data retrieving from DB");
        }
    }

    private void saveHolidaysToDB(List<Holiday> holidays) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            for (Holiday holiday : holidays) {
                session.save(holiday);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("Error occurred during data inserting into DB");
        }
    }
}
