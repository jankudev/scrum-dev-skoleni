package org.scrum.psd.battleship.controller.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ShipTest {

    @Test
    void ship_nohit_isNotSunk() {
        final Ship ship = new Ship("dutchman", 2);
        ship.addShipPart("A1V");
        boolean wasHit = ship.hit(new Position(Letter.B, 2));

        assertFalse(wasHit);
        assertFalse(ship.isSunk());
    }

    @Test
    void ship_partialHit_isNotSunk() {
        final Ship ship = new Ship("dutchman", 2);
        ship.addShipPart("A1H");
        boolean wasHit = ship.hit(new Position(Letter.B, 1));

        assertTrue(wasHit);
        assertFalse(ship.isSunk());
    }

    @Test
    void ship_fullyHit_isSunk() {
        final Ship ship = new Ship("dutchman", 2);
        ship.addShipPart("A1H");
        boolean wasHit1 = ship.hit(new Position(Letter.A, 1));
        boolean wasHit2 = ship.hit(new Position(Letter.B, 1));

        assertTrue(wasHit1);
        assertTrue(wasHit2);
        assertTrue(ship.isSunk());
    }
}
