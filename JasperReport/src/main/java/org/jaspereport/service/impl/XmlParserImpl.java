package org.jaspereport.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.jaspereport.domain.Holiday;
import org.jaspereport.service.XmlParser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XmlParserImpl implements XmlParser {
    private static final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    public static final String HOLYDAYS = "holydays";
    public static final String COUNTRY = "COUNTRY";
    public static final String NAME = "NAME";
    public static final String DATE = "DATE";
    public static final int INDEX = 0;

    public List<Holiday> parseXml(String fileName) {
        List<Holiday> holidayList = new ArrayList<>();
        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new File(fileName));
            NodeList childNodes = doc.getElementsByTagName(HOLYDAYS);
            for (int i = 0; i < childNodes.getLength(); i++) {
                Holiday holiday = getCurrentHoliday(childNodes, i);
                holidayList.add(holiday);
            }
        } catch (ParserConfigurationException | SAXException | IOException e) {
            throw new RuntimeException("Error occurred during XML file parsing. " + e);
        }
        return holidayList;
    }

    private Holiday getCurrentHoliday(NodeList childNodes, int i) {
        Holiday holiday = new Holiday();
        Element element = (Element) childNodes.item(i);
        holiday.setCOUNTRY(element.getElementsByTagName(COUNTRY).item(INDEX).getTextContent());
        holiday.setNAME(element.getElementsByTagName(NAME).item(INDEX).getTextContent());
        holiday.setDATE(element.getElementsByTagName(DATE).item(INDEX).getTextContent());
        return holiday;
    }
}
