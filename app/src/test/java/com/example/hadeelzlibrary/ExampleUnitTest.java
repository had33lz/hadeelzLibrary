package com.example.hadeelzlibrary;

import org.junit.Test;
import static org.junit.Assert.*;

public class ExampleUnitTest {

    // Fine calculation logic
    private double calculateFine(int totalDays) {
        int FREE_DAYS    = 7;
        double FINE_PER_DAY = 0.500;
        int delayDays = Math.max(0, totalDays - FREE_DAYS);
        return delayDays * FINE_PER_DAY;
    }

    // ── Test 1: No fine within free days ──────────────────
    @Test
    public void testNoFine() {
        double fine = calculateFine(5);
        assertEquals(0.0, fine, 0.001);
    }

    // ── Test 2: Exactly 7 days = no fine ─────────────────
    @Test
    public void testExactlySevenDays() {
        double fine = calculateFine(7);
        assertEquals(0.0, fine, 0.001);
    }

    // ── Test 3: 1 day delay = 0.500 OMR ──────────────────
    @Test
    public void testOneDayDelay() {
        double fine = calculateFine(8);
        assertEquals(0.500, fine, 0.001);
    }

    // ── Test 4: 3 days delay = 1.500 OMR ─────────────────
    @Test
    public void testThreeDaysDelay() {
        double fine = calculateFine(10);
        assertEquals(1.500, fine, 0.001);
    }

    // ── Test 5: 7 days delay = 3.500 OMR ─────────────────
    @Test
    public void testSevenDaysDelay() {
        double fine = calculateFine(14);
        assertEquals(3.500, fine, 0.001);
    }
}