package org.scrum.psd.battleship.controller;

import org.scrum.psd.battleship.controller.dto.Color;
import org.scrum.psd.battleship.controller.dto.Letter;
import org.scrum.psd.battleship.controller.dto.Position;
import org.scrum.psd.battleship.controller.dto.Ship;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Random;

public class GameController {

    public static boolean checkIsHit(Collection<Ship> ships, Position shot) {
        if (ships == null) {
            throw new IllegalArgumentException("ships is null");
        }

        if (shot == null) {
            throw new IllegalArgumentException("shot is null");
        }

        for (Ship ship : ships) {
            if (ship.hit(shot)) {
                return true;
            }
        }

        return false;
    }

    public static String getHitMessage(boolean isHit, boolean isPlayer) {
        if (isHit)
            return Color.RED.getColoredText(isPlayer ? "HIT!" : "HIT your ship !");

        return Color.CYAN.getColoredText("Miss!");
    }

    public static List<Ship> initializeShips() {
        return Arrays.asList(
                new Ship("Aircraft Carrier", 5),
                new Ship("Battleship", 4),
                new Ship("Submarine", 3),
                new Ship("Destroyer", 3),
                new Ship("Patrol Boat", 2));
    }

    public static boolean isShipValid(Ship ship) {
        return ship.getShipParts().size() == ship.getSize();
    }

    public static Position getRandomPosition(int size) {
        Random random = new Random();
        Letter letter = Letter.values()[random.nextInt(size)];
        int number = random.nextInt(size);
        Position position = new Position(letter, number);
        return position;
    }

    public static boolean isFleetDestroyed(List<Ship> fleet) {
        return fleet.stream().allMatch(ship -> ship.isSunk());
    }
}
