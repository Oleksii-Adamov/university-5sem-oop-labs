package org.flower.parsexml;

import org.flower.Flower;

public interface FlowerXMLParser {
    Flower parseFlowerFromXML(String xmlPath, String xsdPath);
}
