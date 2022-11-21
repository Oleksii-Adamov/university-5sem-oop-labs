package org.flower.parsexml;

import org.flower.Flower;
import org.xml.sax.SAXException;
import org.xmlvalidation.XMLValidation;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;

public class FlowerSAXXMLParser implements FlowerXMLParser{
    @Override
    public Flower parseFlowerFromXML(String xmlPath, String xsdPath) {
        if (!XMLValidation.validateXML(xsdPath, xmlPath)) {
            return null;
        }
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            FlowerSAXHandler handler = new FlowerSAXHandler();
            saxParser.parse(xmlPath, handler);
            return handler.getParsedFlower();
        } catch (ParserConfigurationException | SAXException | IOException e) {
            System.out.println("Exception: " + e.getMessage());
            return null;
        }

    }
}
