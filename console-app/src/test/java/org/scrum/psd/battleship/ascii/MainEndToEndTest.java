package org.scrum.psd.battleship.ascii;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.contrib.java.lang.system.EnvironmentVariables;
import org.junit.contrib.java.lang.system.ExpectedSystemExit;
import org.junit.contrib.java.lang.system.SystemOutRule;
import org.junit.contrib.java.lang.system.TextFromStandardInputStream;
import org.junit.contrib.java.lang.system.internal.CheckExitCalled;
import org.scrum.psd.battleship.controller.dto.Ship;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.contrib.java.lang.system.TextFromStandardInputStream.emptyStandardInputStream;

public class MainEndToEndTest {
    @ClassRule
    public static final SystemOutRule systemOutRule = new SystemOutRule().enableLog();
    @ClassRule
    public static final TextFromStandardInputStream gameInput = emptyStandardInputStream();
    @ClassRule
    public static final EnvironmentVariables environmentVariables = new EnvironmentVariables();
    @ClassRule
    public static final ExpectedSystemExit exit = ExpectedSystemExit.none();

    @Test
    public void testPlayGameShotHits() {
        environmentVariables.set("debug", "true"); // fixed placement ships

        try {
            gameInput.provideLines("b4");

            Main.main(new String[]{});
        } catch(NoSuchElementException e) {
            Assert.assertTrue(systemOutRule.getLog().contains("Welcome to Battleship"));
            Assert.assertTrue(systemOutRule.getLog().contains("HIT!"));
        }
    }

    @Test
    public void testPlayGameShotMisses() {
        environmentVariables.set("debug", "true"); // fixed placement ships

        try {
            gameInput.provideLines("e4");

            Main.main(new String[]{});
        } catch(NoSuchElementException e) {
            Assert.assertTrue(systemOutRule.getLog().contains("Welcome to Battleship"));
            Assert.assertTrue(systemOutRule.getLog().contains("Miss"));
        }
    }

    @Test
    public void testPlayGameTestingAllPositions_playerWins() {
        environmentVariables.set("debug", "true"); // fixed placement ships
        exit.expectSystemExitWithStatus(0);

        try {
            gameInput.provideLines(
                    "a1", "a2", "a3", "a4", "a5",   // sink Aircraft Carrier
                    "b1", "b2", "b3", "b4",         // sink Battleship
                    "c1", "c2", "c3",               // sink Submarine
                    "d1", "d2", "d3",               // sink Destroyer
                    "e1", "e2"                      // sink Patrol Ship
            );

            Main.main(new String[]{});
        } catch(CheckExitCalled e) {
            Assert.assertTrue(systemOutRule.getLog().contains("Welcome to Battleship"));
            Assert.assertTrue(systemOutRule.getLog().contains("YOU WIN THE GAME !!!"));
        }
    }

    @Test
    public void testPlayGameTestingAllPositions_playerLooses() {
        environmentVariables.set("debug", "true"); // fixed placement ships
        exit.expectSystemExitWithStatus(0);

        try {
            gameInput.provideLines(
                    "a1", "a2", "a3", "a4", "a5",   // sink Aircraft Carrier
                    "b1", "b2", "b3", "b4",         // sink Battleship
                    "c1", "c2", "c3",               // sink Submarine
                    "d1", "d2", "d3",               // sink Destroyer
                    "e1", "e5"                      // almost sink Patrol Ship (miss and let computer win)
            );

            Main.main(new String[]{});
        } catch(CheckExitCalled e) {
            Assert.assertTrue(systemOutRule.getLog().contains("Welcome to Battleship"));
            Assert.assertTrue(systemOutRule.getLog().contains("YOU LOOSE !!!"));
        }
    }
}
