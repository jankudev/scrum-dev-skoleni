package org.scrum.psd.battleship.controller;

import org.scrum.psd.battleship.controller.dto.Color;
import org.scrum.psd.battleship.controller.dto.Letter;
import org.scrum.psd.battleship.controller.dto.Position;
import org.scrum.psd.battleship.controller.dto.Ship;

import java.util.*;
import java.util.stream.Collectors;

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

    public static Position getRandomPosition(int lines, int rows) {
        Random random = new Random();
        Letter letter = Letter.values()[random.nextInt(lines)];
        int number = random.nextInt(rows);
        return new Position(letter, number);
    }

    public static List<Ship> generateShipsRandomPositions(List<Ship> ships, int lines, int rows) {
        for (Ship ship : ships) {
            boolean canBeGenerated = false;
            do {
                Position startPoint = getRandomPosition(lines, rows);
                boolean isVertical = new Random().nextBoolean();

                boolean isThereEnoughSpace = (isVertical && (startPoint.getRow() + ship.getSize()) <= rows) ||
                        (!isVertical & (startPoint.getColumn().ordinal() + ship.getSize()) <= lines);

                if (isThereEnoughSpace) {
                    List<Position> ganeratedFields = new ArrayList<>();
                    if (isVertical) {
                        for (int i = 0; i < ship.getSize(); i++) {
                            ganeratedFields.add(new Position(startPoint.getColumn(), startPoint.getRow() + i));
                        }
                    } else {
                        for (int i = 0; i < ship.getSize(); i++) {
                            ganeratedFields.add(new Position(Letter.values()[startPoint.getColumn().ordinal() + i], startPoint.getRow()));
                        }
                    }

                    List<Position> alreadyGeneratedFleetPositions = ships.stream()
                            .flatMap(x -> x.getPositions().stream())
                            .collect(Collectors.toList());

                    canBeGenerated = ganeratedFields.stream().noneMatch(alreadyGeneratedFleetPositions::contains);

                    if (canBeGenerated)
                        ship.getPositions().addAll(ganeratedFields);
                }
            } while (!canBeGenerated);
        }
        return ships;
    }

    public static boolean isShipValid(Ship ship) {
        return ship.getPositions().size() == ship.getSize();
    }
}
