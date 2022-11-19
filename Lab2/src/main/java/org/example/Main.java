package org.example;

import org.flower.Flower;

public class Main {
    public static void main(String[] args) {
        Flower flower = new Flower();
        if (flower.fromXMLDOM("input.xml", "input.xsd")) {
            System.out.println(flower);
        }
        else {
            System.out.println("XML isn't valid");
        }
    }
}