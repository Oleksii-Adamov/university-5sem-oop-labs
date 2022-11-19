package bouquet;

import accessory.Accessory;
import flower.Flower;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Bouquet {
    private int cost;
    private List<Flower> flowers;
    private List<Accessory> accessories;

    public Bouquet() {
        this.flowers = new ArrayList<>();
        this.accessories = new ArrayList<>();
        this.cost = 0;
    }

    public Bouquet(List<Flower> flowers, List<Accessory> accessories) {
        this.flowers = flowers;
        this.accessories = accessories;
        this.cost = 0;
        if (flowers != null) {
            for (Flower flower : flowers) {
                cost += flower.getCost();
            }
        }
        if (accessories != null) {
            for (Accessory accessory : accessories) {
                cost += accessory.getCost();
            }
        }
    }

    public int getCost() {
        return cost;
    }

    public List<Flower> getFlowers() {
        return flowers;
    }

    public List<Accessory> getAccessories() {
        return accessories;
    }

    public void addFlower(Flower flower) {
        flowers.add(flower);
        cost += flower.getCost();
    }

    public void addAccessory(Accessory accessory) {
        accessories.add(accessory);
        cost += accessory.getCost();
    }

    public void sortFlowersByFreshness() {
        flowers.sort(Comparator.comparingInt(Flower::getDaysAfterGathering));
    }

    // including borders, so [low, high]
    public Flower findFlowerWithStemLenWithinRange(int low, int high) {
        return flowers.stream().filter(flower -> flower.getStemLen() >= low && flower.getStemLen() <= high).findFirst().orElse(null);
    }
}
