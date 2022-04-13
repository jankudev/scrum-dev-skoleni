package org.scrum.psd.battleship.controller;

import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.scrum.psd.battleship.controller.dto.Letter;
import org.scrum.psd.battleship.controller.dto.Position;
import org.scrum.psd.battleship.controller.dto.Ship;
import org.scrum.psd.battleship.controller.dto.ShipPart;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class GameControllerTest {
    @Test
    public void testCheckIsHitTrue() {
        List<Ship> ships = GameController.initializeShips();
        int counter = 0;

        for (Ship ship : ships) {
            Letter letter = Letter.values()[counter];

            for (int i = 0; i < ship.getSize(); i++) {
                ship.getShipParts().add(new ShipPart(new Position(letter, i)));
            }

            counter++;
        }

        boolean result = GameController.checkIsHit(ships, new Position(Letter.A, 1));

        assertTrue(result);
    }

    @Test
    public void testCheckIsHitFalse() {
        List<Ship> ships = GameController.initializeShips();
        int counter = 0;

        for (Ship ship : ships) {
            Letter letter = Letter.values()[counter];

            for (int i = 0; i < ship.getSize(); i++) {
                ship.getShipParts().add(new ShipPart(new Position(letter, i)));
            }

            counter++;
        }

        boolean result = GameController.checkIsHit(ships, new Position(Letter.H, 1));

        assertFalse(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckIsHitPositstionIsNull() {
        GameController.checkIsHit(GameController.initializeShips(), null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckIsHitShipIsNull() {
        GameController.checkIsHit(null, new Position(Letter.H, 1));
    }

    @Test
    public void testIsShipValidFalse() {
        Ship ship = new Ship("TestShip", 3);
        boolean result = GameController.isShipValid(ship);

        assertFalse(result);
    }

    @Test
    public void testIsShipValidTrue() {
        List<Position> positions = Arrays.asList(new Position(Letter.A, 1), new Position(Letter.A, 1), new Position(Letter.A, 1));
        Ship ship = new Ship("TestShip", 3, positions);

        boolean result = GameController.isShipValid(ship);

        assertTrue(result);
    }

    @Test
    public void givenFleet_whenMissed_fleetIsNotDestroyed() {
        final List<Ship> fleet = Arrays.asList(new Ship("ship1", 1, Arrays.asList(new Position(Letter.C, 5))));
        GameController.checkIsHit(fleet, new Position(Letter.B, 1));

        assertFalse(GameController.isFleetDestroyed(fleet));
    }

    @Test
    public void givenSingleNodeFleet_whenShipIsHit_fleetIsDestroyed() {
        final List<Ship> fleet = Arrays.asList(new Ship("ship1", 1, Arrays.asList(new Position(Letter.C, 5))));
        GameController.checkIsHit(fleet, new Position(Letter.C, 5));

        assertTrue(GameController.isFleetDestroyed(fleet));
    }

    @Test
    public void givenMultipleSingleNodeFleet_whenAllAreHit_fleetIsDestroyed() {
        final List<Ship> fleet = Arrays.asList(
                new Ship("ship1", 1, Arrays.asList(new Position(Letter.C, 5))),
                new Ship("ship2", 1, Arrays.asList(new Position(Letter.A, 2))));

        assertFalse("fleet should not be destroyed at this time", GameController.isFleetDestroyed(fleet));
        GameController.checkIsHit(fleet, new Position(Letter.C, 5));

        assertFalse("fleet should not be destroyed at this time", GameController.isFleetDestroyed(fleet));
        GameController.checkIsHit(fleet, new Position(Letter.A, 2));

        assertTrue(GameController.isFleetDestroyed(fleet));
    }

    @Test
    public void testInitializingFleet() {
        List<Ship> ships = GameController.initializeShips();
        Assertions.assertEquals(5, ships.size());
    }

    @Test
    public void testGeneratingFleetPositions() {
        List<Ship> ships = GameController.initializeShips();
        GameController.generateShipsRandomPositions(ships, 8, 8);
        Assertions.assertEquals(
            ships.stream().mapToLong(x->x.getPositions().size()).sum(),
            ships.stream().flatMap(x->x.getPositions().stream()).distinct().count(),
            "All positions should be unique.");
    }

    @Test
    public void validatingGenerateRandomPositions_alwaysInRange() {
        for (int i = 0; i<100000; i++) {
            final Position pos = GameController.getRandomPosition(GameController.boardWidth, GameController.boardHeight);
            assertTrue(pos.getColumn().name().toUpperCase().matches("[A-H]"));
            assertTrue(pos.getRow() >= 1 && pos.getRow() <= 8);
        }
    }

    @Test
    public void givenFleet_whenAllAreHit_fleetIsDestroyed() {
        final List<Ship> fleet = Arrays.asList(
                new Ship("ship1", 1, Arrays.asList(new Position(Letter.C, 5))),
                new Ship("ship2", 2, Arrays.asList(new Position(Letter.A, 2), new Position(Letter.A, 3))));

        assertFalse("fleet should not be destroyed at this time", GameController.isFleetDestroyed(fleet));
        GameController.checkIsHit(fleet, new Position(Letter.C, 5));

        assertFalse("fleet should not be destroyed at this time", GameController.isFleetDestroyed(fleet));
        GameController.checkIsHit(fleet, new Position(Letter.A, 2));

        assertFalse("fleet should not be destroyed at this time", GameController.isFleetDestroyed(fleet));
        GameController.checkIsHit(fleet, new Position(Letter.A, 3));

        assertTrue(GameController.isFleetDestroyed(fleet));
    }
}
