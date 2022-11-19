package org.flower;

public class VisualParameters {
    private String stemColor;
    private String leafColor;
    private double meanSize;

    public VisualParameters(String stemColor, String leafColor, double meanSize) {
        this.stemColor = stemColor;
        this.leafColor = leafColor;
        this.meanSize = meanSize;
    }

    public String getStemColor() {
        return stemColor;
    }

    public void setStemColor(String stemColor) {
        this.stemColor = stemColor;
    }

    public String getLeafColor() {
        return leafColor;
    }

    public void setLeafColor(String leafColor) {
        this.leafColor = leafColor;
    }

    public double getMeanSize() {
        return meanSize;
    }

    public void setMeanSize(double meanSize) {
        this.meanSize = meanSize;
    }

    @Override
    public String toString() {
        return "VisualParameters{" +
                "stemColor='" + stemColor + '\'' +
                ", leafColor='" + leafColor + '\'' +
                ", meanSize=" + meanSize +
                '}';
    }
}
