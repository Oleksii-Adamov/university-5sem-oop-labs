package org.flower;

public class GrowingTips {
    private double temperature;
    private boolean preferLighting;
    private double watering;

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
}
