package org.flower;

import java.util.Objects;

public class GrowingTips {
    private double temperature;
    private boolean preferLighting;
    private double watering;

    public GrowingTips() {}

    public GrowingTips(double temperature, boolean preferLighting, double watering) {
        this.temperature = temperature;
        this.preferLighting = preferLighting;
        this.watering = watering;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public boolean isPreferLighting() {
        return preferLighting;
    }

    public void setPreferLighting(boolean preferLighting) {
        this.preferLighting = preferLighting;
    }

    public double getWatering() {
        return watering;
    }

    public void setWatering(double watering) {
        this.watering = watering;
    }

    @Override
    public String toString() {
        return "GrowingTips{" +
                "temperature=" + temperature +
                ", preferLighting=" + preferLighting +
                ", watering=" + watering +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GrowingTips that = (GrowingTips) o;
        return Double.compare(that.temperature, temperature) == 0 && preferLighting == that.preferLighting && Double.compare(that.watering, watering) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(temperature, preferLighting, watering);
    }
}
