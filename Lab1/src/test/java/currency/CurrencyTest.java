package currency;

import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ThreadLocalRandom;

import static org.junit.jupiter.api.Assertions.*;

class CurrencyTest {

    @Test
    void hryvnias() {
        assertEquals("15 гривень 50 копійнок", Currency.hryvnias(1550));
        assertEquals("9 гривень 0 копійнок", Currency.hryvnias(900));
        assertEquals("0 гривень 40 копійнок", Currency.hryvnias(40));
    }

    @Test
    void hryvniasToCost() {
        assertEquals(1500, Currency.hryvniasToCost(15));
        assertEquals(1650, Currency.hryvniasToCost(16, 50));
        assertEquals(1750, Currency.hryvniasToCost(16, 150));
    }
}