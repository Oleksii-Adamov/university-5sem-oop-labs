package org.flower;

import org.flower.parsexml.FlowerDOMXMLParser;
import org.flower.parsexml.FlowerSAXXMLParser;
import org.flower.parsexml.FlowerStAXXMLParser;
import org.flower.parsexml.FlowerXMLParser;

import java.util.Objects;

public class Flower {
    private String name;
    private Soil soil;
    private String origin;

    private VisualParameters visualParameters;
    private GrowingTips growingTips;
    private Multiplying multiplying;

    public Flower() {}

    public Flower(String name, Soil soil, String origin, VisualParameters visualParameters, GrowingTips growingTips, Multiplying multiplying) {
        this.name = name;
        this.soil = soil;
        this.origin = origin;
        this.visualParameters = visualParameters;
        this.growingTips = growingTips;
        this.multiplying = multiplying;
    }

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

    public VisualParameters getVisualParameters() {
        return visualParameters;
    }

    public void setVisualParameters(VisualParameters visualParameters) {
        this.visualParameters = visualParameters;
    }

    public GrowingTips getGrowingTips() {
        return growingTips;
    }

    public void setGrowingTips(GrowingTips growingTips) {
        this.growingTips = growingTips;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Flower flower = (Flower) o;
        return Objects.equals(name, flower.name) && soil == flower.soil && Objects.equals(origin, flower.origin) &&
                Objects.equals(visualParameters, flower.visualParameters) &&
                Objects.equals(growingTips, flower.growingTips) && multiplying == flower.multiplying;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, soil, origin, visualParameters, growingTips, multiplying);
    }

    private void copyFlower(Flower otherFlower) {
        this.name = otherFlower.name;
        this.soil = otherFlower.soil;
        this.origin = otherFlower.origin;
        this.visualParameters = otherFlower.visualParameters;
        this.growingTips = otherFlower.growingTips;
        this.multiplying = otherFlower.multiplying;
    }

    public boolean fromXML(String xmlPath, String xsdPath, FlowerXMLParser parser) {
        Flower parsedFlower = parser.parseFlowerFromXML(xmlPath, xsdPath);
        if (parsedFlower == null) {
            return false;
        }
        // copy contents of parsedFlower to this
        copyFlower(parsedFlower);
        return true;
    }

    public boolean fromXMLDOM(String xmlPath, String xsdPath) {
        return fromXML(xmlPath, xsdPath, new FlowerDOMXMLParser());
    }

    public boolean fromXMLSAX(String xmlPath, String xsdPath) {
        return fromXML(xmlPath, xsdPath, new FlowerSAXXMLParser());
    }

    public boolean fromXMLStAX(String xmlPath, String xsdPath) {
        return fromXML(xmlPath, xsdPath, new FlowerStAXXMLParser());
    }

}
