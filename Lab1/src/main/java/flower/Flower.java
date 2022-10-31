package flower;

import currency.Currency;

public class Flower {
    private String speciesName;
    private Color color;
    private int daysAfterGathering; // freshness
    private int stemLen; // in cm
    private int budDiam; // in cm
    private int cost; // у копійках
    public Flower(String speciesName, Color color, int daysAfterGathering, int stemLen, int budDiam, int cost) {
        this.speciesName = speciesName;
        this.color = color;
        this.daysAfterGathering = daysAfterGathering;
        this.stemLen = stemLen;
        this.budDiam = budDiam;
        this.cost = cost;
    }

    public String getSpeciesName() {
        return speciesName;
    }

    public void setSpeciesName(String speciesName) {
        this.speciesName = speciesName;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public int getDaysAfterGathering() {
        return daysAfterGathering;
    }

    public void setDaysAfterGathering(int daysAfterGathering) {
        this.daysAfterGathering = daysAfterGathering;
    }

    public int getStemLen() {
        return stemLen;
    }

    public void setStemLen(int stemLen) {
        this.stemLen = stemLen;
    }

    public int getBudDiam() {
        return budDiam;
    }

    public void setBudDiam(int budDiam) {
        this.budDiam = budDiam;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    @Override
    public String toString() {
        return "Flower{" +
                "speciesName='" + speciesName + '\'' +
                ", color=" + color +
                ", daysAfterGathering=" + daysAfterGathering +
                ", stemLen=" + stemLen + "cm" +
                ", budDiam=" + budDiam + "cm" +
                ", cost=" + Currency.hryvnias(cost) +
                '}';
    }
}
