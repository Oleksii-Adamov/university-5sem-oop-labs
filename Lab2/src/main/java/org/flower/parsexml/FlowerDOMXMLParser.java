package org.flower.parsexml;

import org.flower.Flower;
import org.flower.GrowingTips;
import org.flower.ParseEnum;
import org.flower.VisualParameters;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import org.xmlvalidation.XMLValidation;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

public class FlowerDOMXMLParser implements FlowerXMLParser {

    @Override
    public Flower parseFlowerFromXML(String xmlPath, String xsdPath) {
        Flower flower;
        if (!XMLValidation.validateXML(xsdPath, xmlPath)) {
            return null;
        }
        try {
            flower = new Flower();
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new File(xmlPath));
            Element root = doc.getDocumentElement();
            flower.setName(root.getElementsByTagName(FlowerTags.NAME_TAG).item(0).getChildNodes().item(0).getNodeValue());
            flower.setSoil(ParseEnum.parseSoil(root.getElementsByTagName(FlowerTags.SOIL_TAG).item(0).getChildNodes().item(0).getNodeValue()));
            flower.setOrigin(root.getElementsByTagName(FlowerTags.ORIGIN_TAG).item(0).getChildNodes().item(0).getNodeValue());
            flower.setVisualParameters(new VisualParameters(
                    root.getElementsByTagName(FlowerTags.STEM_COLOR_TAG).item(0).getChildNodes().item(0).getNodeValue(),
                    root.getElementsByTagName(FlowerTags.LEAF_COLOR_TAG).item(0).getChildNodes().item(0).getNodeValue(),
                    Double.parseDouble(root.getElementsByTagName(FlowerTags.MEAN_SIZE_TAG).item(0).getChildNodes().item(0).getNodeValue())));
            flower.setGrowingTips(new GrowingTips(
                    Double.parseDouble(root.getElementsByTagName(FlowerTags.TEMPERATURE_TAG).item(0).getChildNodes().item(0).getNodeValue()),
                    Boolean.parseBoolean(root.getElementsByTagName(FlowerTags.PREFER_LIGHTING_TAG).item(0).getChildNodes().item(0).getNodeValue()),
                    Double.parseDouble(root.getElementsByTagName(FlowerTags.WATERING_TAG).item(0).getChildNodes().item(0).getNodeValue())));
            flower.setMultiplying(ParseEnum.parseMultiplying(root.getElementsByTagName(FlowerTags.MULTIPLYING_TAG).item(0).getChildNodes().item(0).getNodeValue()));
        } catch (IOException | ParserConfigurationException | SAXException e) {
            System.out.println("Exception: " + e.getMessage());
            return null;
        }
        return flower;
    }
}
