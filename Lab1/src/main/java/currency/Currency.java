package currency;

public class Currency {

    public static String hryvnias(int cost) {
        return cost/100 + " гривень " + cost % 100 + " копійнок";
    }

    public static int hryvniasToCost(int hryvnias, int kopiykas) {
        return hryvnias * 100 + kopiykas;
    }

    public static int hryvniasToCost(int hryvnias) {
        return hryvnias * 100;
    }
}
