package org.scrum.psd.battleship.controller;

import org.scrum.psd.battleship.controller.dto.*;

import java.util.*;
import java.util.stream.Collectors;

public class GameController {

    public static final int boardWidth = Letter.values().length;
    public static final int boardHeight = 8;

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
        int number = random.nextInt(rows) + 1; // ordering of positions starts from 1
        return new Position(letter, number);
    }

    public static List<Ship> generateShipsRandomPositions(List<Ship> ships, int lines, int rows) {
        for (Ship ship : ships) {
            boolean wasPlaced = false;
            do {
                Position startPoint = getRandomPosition(lines, rows);
                boolean isVertical = new Random().nextBoolean();

                boolean isThereEnoughSpace = isThereEnoughSpace(lines, rows, ship, startPoint, isVertical);

                if (isThereEnoughSpace) {
                    List<Position> generatedFields = getShipPositions(startPoint, isVertical, ship.getSize());
                    List<Position> alreadyTakenPositions = getAlreadyTakenPositions(ships);

                    if (Validator.isValidShipPlacement(generatedFields, alreadyTakenPositions)) {
                        ship.getShipParts().addAll(generatedFields.stream().map(ShipPart::new).collect(Collectors.toList()));
                        wasPlaced = true;
                    }
                }
            } while (!wasPlaced);
        }
        return ships;
    }

    private static List<Position> getAlreadyTakenPositions(List<Ship> ships) {
        return ships.stream()
                .flatMap(x -> x.getShipParts().stream().map(ShipPart::getPosition).collect(Collectors.toList()).stream())
                .collect(Collectors.toList());
    }

    public static List<Position> getShipPositions(Position startPoint, boolean isVertical, int size) {
        List<Position> generatedFields = new ArrayList<>();
        if (isVertical) {
            for (int i = 0; i < size; i++) {
                generatedFields.add(new Position(startPoint.getColumn(), startPoint.getRow() + i));
            }
        } else {
            for (int i = 0; i < size; i++) {
                generatedFields.add(new Position(Letter.values()[startPoint.getColumn().ordinal() + i], startPoint.getRow()));
            }
        }
        return generatedFields;
    }

    private static boolean isThereEnoughSpace(int lines, int rows, Ship ship, Position startPoint, boolean isVertical) {
        return (isVertical && (startPoint.getRow() + ship.getSize()) <= rows) ||
                (!isVertical & (startPoint.getColumn().ordinal() + ship.getSize()) <= lines);
    }

    public static boolean isShipValid(Ship ship) {
        return ship.getShipParts().size() == ship.getSize();
    }

    public static boolean isFleetDestroyed(List<Ship> fleet) {
        return fleet.stream().allMatch(ship -> ship.isSunk());
    }

    public static Position parsePosition(String input) {
        Letter letter = Letter.valueOf(input.toUpperCase().substring(0, 1));
        int number = Integer.parseInt(input.substring(1));
        return new Position(letter, number);
    }

    public static ShipPosition parseShipPosition(String input) {
        Letter letter = Letter.valueOf(input.toUpperCase().substring(0, 1));
        int number = Integer.parseInt(input.substring(1, 2));
        String orientation = input.substring(input.length() - 1);
        boolean isVertical;
        switch (orientation.toUpperCase(Locale.ROOT)) {
            case "V":
                isVertical = true;
                break;

            case "H":
                isVertical = false;
                break;

            default:
                throw new RuntimeException();
        }
        return new ShipPosition(new Position(letter, number), isVertical);
    }
}
