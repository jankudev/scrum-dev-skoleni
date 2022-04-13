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
import org.scrum.psd.battleship.controller.dto.debug.DebugFixedPositions;

import java.util.*;

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
            gameInput.provideLines("a1");

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

    @Ignore
    @Test
    public void testPlayGameTestingAllPositions_playerWins() {
        environmentVariables.set("debug", "true"); // fixed placement ships
        exit.expectSystemExitWithStatus(0);

        try {
            gameInput.provideLines(DebugFixedPositions.DEFAULT_FIXED_POSITIONS.split(" "));

            Main.main(new String[]{});
        } catch(CheckExitCalled e) {
            Assert.assertTrue(systemOutRule.getLog().contains("Welcome to Battleship"));
            Assert.assertTrue(systemOutRule.getLog().contains("YOU WIN THE GAME !!!"));
        }
    }

    @Ignore
    @Test
    public void testPlayGameTestingAllPositions_playerLooses() {
        environmentVariables.set("debug", "true"); // fixed placement ships
        exit.expectSystemExitWithStatus(0);

        try {
            gameInput.provideLines(
                    String.join(" ",
                            "h8 h7 h2",
                            DebugFixedPositions.DEFAULT_MISSES,
                            DebugFixedPositions.DEFAULT_FIXED_POSITIONS).split(" "));

            Main.main(new String[]{});
        } catch(CheckExitCalled e) {
            Assert.assertTrue(systemOutRule.getLog().contains("Welcome to Battleship"));
            Assert.assertTrue(systemOutRule.getLog().contains("YOU LOOSE !!!"));
        }
    }

    @Test
    public void playGameWithRandomGuessing_untilSomeoneWins() {
        exit.expectSystemExitWithStatus(0);

        final List<String> positions = new ArrayList<>();
        for (String s : "abcdefgh".split("")) {
            for (int i = 1; i<9; i++) {
                positions.add(s + i);
            }
        }
        Collections.shuffle(positions);
        final String positionsAsString = String.join(" ",
                DebugFixedPositions.DEFAULT_FIXED_POSITIONS, String.join(" ", positions));

        try {
            gameInput.provideLines(positionsAsString.split(" "));

            Main.main(new String[]{});
        } catch(CheckExitCalled e) {
            Assert.assertTrue(systemOutRule.getLog().contains("Welcome to Battleship"));
            Assert.assertTrue(systemOutRule.getLog().contains("YOU LOOSE !!!") || systemOutRule.getLog().contains("YOU WIN THE GAME !!!"));
        }
    }
}
