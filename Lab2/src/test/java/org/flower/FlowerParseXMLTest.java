package org.flower;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FlowerParseXMLTest {

    private static final String xmlPath = "input.xml";
    private static final String xsdPath = "input.xsd";

    private static final String invalidXmlPath = "invalid_input.xml";
    private static final Flower expectedFlower = new Flower("Rose", Soil.PODZOL, "Ukraine",
            new VisualParameters("green", "red", 50),
            new GrowingTips(20, true, 100), Multiplying.SEEDS);

    private Flower parsedFlower = new Flower();

    @Test
    void fromXMLDOMTest() {
        assertTrue(parsedFlower.fromXMLDOM(xmlPath, xsdPath));
        assertEquals(expectedFlower, parsedFlower);

        assertFalse(parsedFlower.fromXMLDOM(invalidXmlPath, xsdPath));
    }

    @Test
    void fromXMLSAXTest() {
        assertTrue(parsedFlower.fromXMLSAX(xmlPath, xsdPath));
        assertEquals(expectedFlower, parsedFlower);

        assertFalse(parsedFlower.fromXMLSAX(invalidXmlPath, xsdPath));
    }

    @Test
    void fromXMLStAXTest() {
        assertTrue(parsedFlower.fromXMLStAX(xmlPath, xsdPath));
        assertEquals(expectedFlower, parsedFlower);

        assertFalse(parsedFlower.fromXMLStAX(invalidXmlPath, xsdPath));
    }
}