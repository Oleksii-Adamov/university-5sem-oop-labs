package org.flower.parsexml;

import org.flower.Flower;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import org.xmlvalidation.XMLValidation;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXResult;
import java.io.File;
import java.io.IOException;

public class FlowerDOMXMLParser implements FlowerXMLParser {

    @Override
    public Flower parseFlowerFromXML(String xmlPath, String xsdPath) {
        if (!XMLValidation.validateXML(xsdPath, xmlPath)) {
            return null;
        }
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new File(xmlPath));

            FlowerSAXHandler handler = new FlowerSAXHandler();

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.transform(new DOMSource(doc),
                    new SAXResult(handler));

            return handler.getParsedFlower();
        } catch (IOException | ParserConfigurationException | SAXException | TransformerException e) {
            System.out.println("Exception: " + e.getMessage());
            return null;
        }
    }
}
