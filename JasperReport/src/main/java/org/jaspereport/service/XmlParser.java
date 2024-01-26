package org.jaspereport.service;

import org.jaspereport.domain.Holiday;

import java.util.List;

public interface XmlParser {
    List<Holiday> parseXml(String fileName);
}
