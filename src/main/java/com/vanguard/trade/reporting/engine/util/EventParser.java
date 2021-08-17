package com.vanguard.trade.reporting.engine.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class EventParser {

    private DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    private XPath xPath = XPathFactory.newInstance().newXPath();
    private DocumentBuilder builder = null;
    public EventParser() {
        factory.setNamespaceAware(false);
        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            log.error(e.getMessage());
        }
    }

    public Map<String, String> getExpressionValues(List<String> expressionList, String fileName, InputStream inputStream) {

        Map<String, String> expressionValues = new HashMap<>();
        try {
            Document xmlDocument = builder.parse(inputStream);

            expressionList.stream().forEach((expression) -> {
                try {
                    Object value = xPath.compile(expression).evaluate(xmlDocument, XPathConstants.STRING);
                    if (value != null) {
                        log.debug(expression + " - " + value + " extracted from the file " + fileName);
                        expressionValues.put(expression, (String) value);
                    }
                } catch (XPathExpressionException e) {
                    log.error(e.getMessage());
                }
            });

        } catch (IOException e) {
            log.error(e.getMessage());
        } catch (SAXException e) {
            log.error(e.getMessage());
        }
            return expressionValues;
    }

    public Map<String, String> getExpressionValues(List<String> expressionList, String xmlFilePath) {

        Map<String, String> expressionValues = new HashMap<>();
        try {
            Document xmlDocument = builder.parse(new File(xmlFilePath));

            expressionList.stream().forEach((expression) -> {
                try {
                    Object value = xPath.compile(expression).evaluate(xmlDocument, XPathConstants.STRING);
                    if (value != null) {
                        log.info(expression + " - " + value + " extracted from the file " + xmlFilePath);
                        expressionValues.put(expression, (String) value);
                    }
                } catch (XPathExpressionException e) {
                    log.error(e.getMessage());
                }
            });

        } catch (IOException e) {
            log.error(e.getMessage());
        } catch (SAXException e) {
            log.error(e.getMessage());
        }
            return expressionValues;
    }

    // Write Unit Tests...
    public static void main(String argv[]) {
        EventParser eventParser = new EventParser();
        List<String> xPathList = new ArrayList();
        xPathList.add("//buyerPartyReference/@href");
        xPathList.add("//sellerPartyReference/@href");
        xPathList.add("//paymentAmount/amount");
        xPathList.add("//paymentAmount/currency");
        System.out.println(eventParser.getExpressionValues(xPathList, "/Users/muhammad.javaid/Downloads/event XML 2/event6.xml"));

    }
}
