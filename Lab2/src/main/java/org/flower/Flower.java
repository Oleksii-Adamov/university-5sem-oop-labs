package org.flower;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import org.xmlvalidation.XMLValidation;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

public class Flower {
    private String name;
    private Soil soil;
    private String origin;

    private VisualParameters visualParameters;
    private GrowingTips growingTips;
    private Multiplying multiplying;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Soil getSoil() {
        return soil;
    }

    public void setSoil(Soil soil) {
        this.soil = soil;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public Multiplying getMultiplying() {
        return multiplying;
    }

    public void setMultiplying(Multiplying multiplying) {
        this.multiplying = multiplying;
    }

    @Override
    public String toString() {
        return "Flower{" +
                "name='" + name + '\'' +
                ", soil=" + soil +
                ", origin='" + origin + '\'' +
                ", visualParameters=" + visualParameters +
                ", growingTips=" + growingTips +
                ", multiplying=" + multiplying +
                '}';
    }

    public boolean fromXMLDOM(String xmlPath, String xsdPath) {
        if (!XMLValidation.validateXML(xsdPath, xmlPath)) {
            return false;
        }
        try {
            DocumentBuilder db = null;
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            db = dbf.newDocumentBuilder();
            Document doc = null;
            doc = db.parse(new File(xmlPath));
            Element root = doc.getDocumentElement();
            name = root.getElementsByTagName("name").item(0).getChildNodes().item(0).getNodeValue();
            soil = ParseEnum.parseSoil(root.getElementsByTagName("soil").item(0).getChildNodes().item(0).getNodeValue());
            origin = root.getElementsByTagName("origin").item(0).getChildNodes().item(0).getNodeValue();
            visualParameters = new VisualParameters(
                    root.getElementsByTagName("stemColor").item(0).getChildNodes().item(0).getNodeValue(),
                    root.getElementsByTagName("leafColor").item(0).getChildNodes().item(0).getNodeValue(),
                    Double.parseDouble(root.getElementsByTagName("meanSize").item(0).getChildNodes().item(0).getNodeValue()));
            growingTips = new GrowingTips(
                    Double.parseDouble(root.getElementsByTagName("temperature").item(0).getChildNodes().item(0).getNodeValue()),
                    Boolean.parseBoolean(root.getElementsByTagName("preferLighting").item(0).getChildNodes().item(0).getNodeValue()),
                    Double.parseDouble(root.getElementsByTagName("watering").item(0).getChildNodes().item(0).getNodeValue()));
            multiplying = ParseEnum.parseMultiplying(root.getElementsByTagName("multiplying").item(0).getChildNodes().item(0).getNodeValue());
        } catch (IOException | ParserConfigurationException | SAXException e) {
            System.out.println("Exception: " + e.getMessage());
            return false;
        }
        return true;
    }

}
