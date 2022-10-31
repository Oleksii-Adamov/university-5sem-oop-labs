package demo;

import accessory.Accessory;
import accessory.Material;
import bouquet.Bouquet;
import flower.Color;
import flower.Flower;

public class Demo {
    public static void execute() {
        Flower rose = new Flower("Rose", Color.RED, 0, 40, 8, 5000);
        Flower tulip = new Flower("Tulip", Color.YELLOW, 1, 30, 4, 2500);
        Flower chamomile = new Flower("Chamomile", Color.WHITE, 2, 30, 10, 1000);
        Accessory ribbon = new Accessory(500, Material.FOIL, 10);
        Accessory wrapper = new Accessory(1500, Material.PAPER, 160);
        Bouquet bouquet = new Bouquet();
        bouquet.addFlower(tulip);
        bouquet.addFlower(chamomile);
        bouquet.addFlower(rose);
        bouquet.addAccessory(wrapper);
        bouquet.addAccessory(ribbon);
        System.out.println("Sorted by freshness:");
        bouquet.sortFlowersByFreshness();
        for (Flower flower : bouquet.getFlowers()) {
            System.out.println(flower);
        }
        System.out.println();
        System.out.println("Flower with stem length [35, 45] is " + bouquet.findFlowerWithStemLenWithinRange(35, 45));
    }
}
