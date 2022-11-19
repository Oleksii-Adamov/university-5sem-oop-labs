package org.flower;

public class ParseEnum {
    private ParseEnum() {}
    public static Soil parseSoil(String string) {
        return switch (string) {
            case "podzol" -> Soil.PODZOL;
            case "dirt" -> Soil.DIRT;
            case "sod-podzol" -> Soil.SOD_POZOL;
            default -> null;
        };
    }

    public static Multiplying parseMultiplying(String string) {
        return switch (string) {
            case "leafs" -> Multiplying.LEAFS;
            case "seeds" -> Multiplying.SEEDS;
            case "cuttings" -> Multiplying.CUTTINGS;
            default -> null;
        };
    }

}
