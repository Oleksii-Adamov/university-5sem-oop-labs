package accessory;

import currency.Currency;

public class Accessory {
    private int cost; // у копійках
    private Material material;
    private int area; // in cm^2

    public Accessory() {

    }

    public Accessory(int cost, Material material, int area) {
        this.cost = cost;
        this.material = material;
        this.area = area;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public int getArea() {
        return area;
    }

    public void setArea(int area) {
        this.area = area;
    }

    @Override
    public String toString() {
        return "Accessory{" +
                "cost=" + Currency.hryvnias(cost) +
                ", material=" + material +
                ", area=" + area + " cm^2" +
                '}';
    }
}
