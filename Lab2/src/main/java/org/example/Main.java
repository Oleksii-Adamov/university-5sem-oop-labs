package org.example;

import org.flower.Flower;

public class Main {
    public static void main(String[] args) {
        Flower flower = new Flower();
        System.out.println("DOM");
        if (flower.fromXMLDOM("input.xml", "input.xsd")) {
            System.out.println(flower);
        }
        else {
            System.out.println("XML isn't valid");
        }
        System.out.println("SAX");
        if (flower.fromXMLSAX("input.xml", "input.xsd")) {
            System.out.println(flower);
        }
        else {
            System.out.println("XML isn't valid");
        }
        System.out.println("StAX");
        if (flower.fromXMLStAX("input.xml", "input.xsd")) {
            System.out.println(flower);
        }
        else {
            System.out.println("XML isn't valid");
        }
    }
}