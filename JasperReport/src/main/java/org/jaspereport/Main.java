package org.jaspereport;

import org.jaspereport.service.impl.ReportProviderImpl;

public class Main {
    public static void main(String[] args) {
        ReportProviderImpl provider = new ReportProviderImpl();
        provider.viewReport();
    }
}