package com.scrapperescapetime;

import static org.junit.Assert.*;
import org.junit.jupiter.api.*;

public class ScrapperTest {
    public static Scrapper leScrapper;
    public static RecupererIdentifiant allan;

    @BeforeAll
    public static void setUp() {
        leScrapper = new Scrapper("fake", "fake", "lemans");
        allan = new RecupererIdentifiant();
    }

    
}