package bouquet;

import accessory.Accessory;
import accessory.Material;
import flower.Color;
import flower.Flower;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.jupiter.api.Assertions.*;

class BouquetTest {

    private int testSize = 100;
    private int numRandTests = 10;

    private Flower randomFlower() {
        return new Flower("test", Color.RED, ThreadLocalRandom.current().nextInt(10, 30),
                ThreadLocalRandom.current().nextInt(5, 50),
                ThreadLocalRandom.current().nextInt(1, 50),
                ThreadLocalRandom.current().nextInt(1, 10000));
    }

    private Accessory randomAccessory() {
        return new Accessory(ThreadLocalRandom.current().nextInt(10, 10000),
                ThreadLocalRandom.current().nextInt(0, 2) == 0 ? Material.FOIL : Material.PAPER,
                ThreadLocalRandom.current().nextInt(1, 10000));
    }

    private Flower[] randomFlowers(int size) {
        Flower[] flowers = new Flower[size];
        for (int i = 0; i < size; i++) {
            flowers[i] = randomFlower();
        }
        return flowers;
    }

    private Accessory[] randomAccessories(int size) {
        Accessory[] accessories = new Accessory[size];
        for (int i = 0; i < size; i++) {
            accessories[i] = randomAccessory();
        }
        return accessories;
    }

    @Test
    void getFlowers() {
        Flower[] flowers = randomFlowers(testSize);
        Bouquet bouquet = new Bouquet(List.of(flowers), null);
        assertEquals(List.of(flowers), bouquet.getFlowers());
    }

    @Test
    void getAccessories() {
        Accessory[] accessories = randomAccessories(testSize);
        Bouquet bouquet = new Bouquet(null, List.of(accessories));
        assertEquals(List.of(accessories), bouquet.getAccessories());
    }

    @Test
    void getCost() {
        for (int t = 0; t < numRandTests; t++) {
            int cost = 0;
            Flower[] flowers = new Flower[testSize / 2 + testSize % 2];
            Accessory[] accessories = new Accessory[testSize / 2];
            for (int i = 0; i < testSize / 2; i++) {
                flowers[i] = randomFlower();
                accessories[i] = randomAccessory();
                cost += flowers[i].getCost() + accessories[i].getCost();
            }
            if (testSize % 2 == 1) flowers[testSize / 2] = randomFlower();
            Bouquet bouquet = new Bouquet(List.of(flowers), List.of(accessories));
            assertEquals(cost, bouquet.getCost());
        }
    }

    @Test
    void addFlower() {
        for (int t = 0; t < numRandTests; t++) {
            Bouquet bouquet = new Bouquet();
            Flower[] flowers = new Flower[testSize];
            for (int i = 0; i < testSize; i++) {
                Flower flower = randomFlower();
                flowers[i] = flower;
                bouquet.addFlower(flower);
                assertEquals(i + 1, bouquet.getFlowers().size());
                int cost = 0;
                for (int j = 0; j <= i; j++) {
                    assertTrue(bouquet.getFlowers().contains(flowers[j]));
                    cost += flowers[j].getCost();
                }
                assertEquals(cost, bouquet.getCost());
            }
        }
    }

    @Test
    void addAccessory() {
        for (int t = 0; t < numRandTests; t++) {
            Bouquet bouquet = new Bouquet();
            Accessory[] accessories = new Accessory[testSize];
            for (int i = 0; i < testSize; i++) {
                Accessory accessory = randomAccessory();
                accessories[i] = accessory;
                bouquet.addAccessory(accessory);
                assertEquals(i + 1, bouquet.getAccessories().size());
                int cost = 0;
                for (int j = 0; j <= i; j++) {
                    assertTrue(bouquet.getAccessories().contains(accessories[j]));
                    cost += accessories[j].getCost();
                }
                assertEquals(cost, bouquet.getCost());
            }
        }
    }

    @Test
    void sortFlowersByFreshness() {
        for (int t = 0; t < numRandTests; t++) {
            Bouquet bouquet = new Bouquet();
            for (int i = 0; i < testSize; i++) {
                bouquet.addFlower(randomFlower());
            }
            bouquet.sortFlowersByFreshness();
            for (int i = 1; i < testSize; i++) {
                assertTrue(bouquet.getFlowers().get(i - 1).getDaysAfterGathering() <= bouquet.getFlowers().get(i).getDaysAfterGathering());
            }
        }
    }

    @Test
    void findFlowerWithStemLenWithinRange() {
        Bouquet bouquet = new Bouquet();
        Flower rose = new Flower("Rose", Color.RED, 0, 40, 8, 5000);
        Flower tulip = new Flower("Tulip", Color.YELLOW, 1, 30, 4, 2500);
        Flower chamomile = new Flower("Chamomile", Color.WHITE, 2, 30, 10, 1000);
        bouquet.addFlower(rose);
        bouquet.addFlower(tulip);
        bouquet.addFlower(chamomile);

        assertEquals(rose, bouquet.findFlowerWithStemLenWithinRange(35, 45));
        assertEquals(tulip, bouquet.findFlowerWithStemLenWithinRange(25, 35));
        assertNull(bouquet.findFlowerWithStemLenWithinRange(10, 15));
    }
}