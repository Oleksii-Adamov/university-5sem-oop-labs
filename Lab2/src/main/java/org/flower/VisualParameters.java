package org.flower;

import java.util.Objects;

public class VisualParameters {
    private String stemColor;
    private String leafColor;
    private double meanSize;

    public VisualParameters() {}

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VisualParameters that = (VisualParameters) o;
        return Double.compare(that.meanSize, meanSize) == 0 && Objects.equals(stemColor, that.stemColor) && Objects.equals(leafColor, that.leafColor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stemColor, leafColor, meanSize);
    }
}
