package org.flower.parsexml;

import org.flower.Flower;
import org.flower.GrowingTips;
import org.flower.ParseEnum;
import org.flower.VisualParameters;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class FlowerSAXHandler extends DefaultHandler {

    private Flower parsedFlower = new Flower();

    private VisualParameters parsedVisualParameters = new VisualParameters();

    private GrowingTips parsedGrowingTips = new GrowingTips();
    private StringBuilder elementValue;

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (elementValue == null) {
            elementValue = new StringBuilder();
        } else {
            elementValue.append(ch, start, length);
        }
    }

    @Override
    public void startElement(String uri, String lName, String qName, Attributes attr) throws SAXException {
        elementValue = new StringBuilder();
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        switch (qName) {
            case FlowerTags.NAME_TAG -> parsedFlower.setName(elementValue.toString());
            case FlowerTags.SOIL_TAG -> parsedFlower.setSoil(ParseEnum.parseSoil(elementValue.toString()));
            case FlowerTags.ORIGIN_TAG -> parsedFlower.setOrigin(elementValue.toString());
            case FlowerTags.STEM_COLOR_TAG -> parsedVisualParameters.setStemColor(elementValue.toString());
            case FlowerTags.LEAF_COLOR_TAG -> parsedVisualParameters.setLeafColor(elementValue.toString());
            case FlowerTags.MEAN_SIZE_TAG ->
                    parsedVisualParameters.setMeanSize(Double.parseDouble(elementValue.toString()));
            case FlowerTags.TEMPERATURE_TAG ->
                    parsedGrowingTips.setTemperature(Double.parseDouble(elementValue.toString()));
            case FlowerTags.PREFER_LIGHTING_TAG ->
                    parsedGrowingTips.setPreferLighting(Boolean.parseBoolean(elementValue.toString()));
            case FlowerTags.WATERING_TAG -> parsedGrowingTips.setWatering(Double.parseDouble(elementValue.toString()));
            case FlowerTags.MULTIPLYING_TAG ->
                    parsedFlower.setMultiplying(ParseEnum.parseMultiplying(elementValue.toString()));
        }
    }

    @Override
    public void endDocument() throws SAXException {
        parsedFlower.setVisualParameters(parsedVisualParameters);
        parsedFlower.setGrowingTips(parsedGrowingTips);
    }

    public Flower getParsedFlower() {
        return parsedFlower;
    }
}
