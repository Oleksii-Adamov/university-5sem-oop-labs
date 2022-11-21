package org.flower.parsexml;

import org.flower.Flower;
import org.flower.GrowingTips;
import org.flower.ParseEnum;
import org.flower.VisualParameters;
import org.xmlvalidation.XMLValidation;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class FlowerStAXXMLParser implements FlowerXMLParser {
    @Override
    public Flower parseFlowerFromXML(String xmlPath, String xsdPath) {
        if (!XMLValidation.validateXML(xsdPath, xmlPath)) {
            return null;
        }
        try {
            Flower parsedFlower = new Flower();
            VisualParameters parsedVisualParameters = new VisualParameters();
            GrowingTips parsedGrowingTips = new GrowingTips();
            XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
            XMLEventReader reader = xmlInputFactory.createXMLEventReader(new FileInputStream(xmlPath));
            while (reader.hasNext()) {
                XMLEvent nextEvent = reader.nextEvent();
                if (nextEvent.isStartElement()) {
                    StartElement startElement = nextEvent.asStartElement();
                    nextEvent = reader.nextEvent();
                    switch (startElement.getName().getLocalPart()) {
                        case FlowerTags.NAME_TAG -> parsedFlower.setName(nextEvent.asCharacters().getData());
                        case FlowerTags.SOIL_TAG -> parsedFlower.setSoil(ParseEnum.parseSoil(nextEvent.asCharacters().getData()));
                        case FlowerTags.ORIGIN_TAG -> parsedFlower.setOrigin(nextEvent.asCharacters().getData());
                        case FlowerTags.STEM_COLOR_TAG -> parsedVisualParameters.setStemColor(nextEvent.asCharacters().getData());
                        case FlowerTags.LEAF_COLOR_TAG -> parsedVisualParameters.setLeafColor(nextEvent.asCharacters().getData());
                        case FlowerTags.MEAN_SIZE_TAG ->
                                parsedVisualParameters.setMeanSize(Double.parseDouble(nextEvent.asCharacters().getData()));
                        case FlowerTags.TEMPERATURE_TAG ->
                                parsedGrowingTips.setTemperature(Double.parseDouble(nextEvent.asCharacters().getData()));
                        case FlowerTags.PREFER_LIGHTING_TAG ->
                                parsedGrowingTips.setPreferLighting(Boolean.parseBoolean(nextEvent.asCharacters().getData()));
                        case FlowerTags.WATERING_TAG -> parsedGrowingTips.setWatering(Double.parseDouble(nextEvent.asCharacters().getData()));
                        case FlowerTags.MULTIPLYING_TAG ->
                                parsedFlower.setMultiplying(ParseEnum.parseMultiplying(nextEvent.asCharacters().getData()));
                    }
                }
            }
            parsedFlower.setVisualParameters(parsedVisualParameters);
            parsedFlower.setGrowingTips(parsedGrowingTips);
            return parsedFlower;
        } catch (XMLStreamException | FileNotFoundException e) {
            System.out.println("Exception: " + e.getMessage());
            return null;
        }
    }
}
