package org.scrum.psd.battleship.controller;

import org.scrum.psd.battleship.controller.dto.Letter;
import org.scrum.psd.battleship.controller.dto.Position;

import java.util.List;
import java.util.stream.Stream;

public class Validator {

    public static boolean isValidShipPlacement(List<Position> generatedFields, List<Position> alreadyTakenPositions) {
        if (isThereConflict(generatedFields, alreadyTakenPositions)) {
            return false;
        }

        if (isAdjacent(generatedFields, alreadyTakenPositions)) {
            return false;
        }

        return true;
    }

    private static boolean isThereConflict(List<Position> generatedFields, List<Position> alreadyTakenPositions) {
        return generatedFields.stream().anyMatch(alreadyTakenPositions::contains);
    }

    private static boolean isAdjacent(List<Position> generatedFields, List<Position> alreadyTakenPositions) {
        return generatedFields.stream().flatMap(Validator::getAdjacentFields).anyMatch(alreadyTakenPositions::contains);
    }

    private static Stream<Position> getAdjacentFields(Position pos) {
        Position right = null;
        Position left = null;
        if (Letter.values().length > pos.getColumn().ordinal() + 1)
            right = new Position(Letter.values()[pos.getColumn().ordinal() + 1], pos.getRow());

        if (0 < pos.getColumn().ordinal())
            left = new Position(Letter.values()[pos.getColumn().ordinal() - 1], pos.getRow());


        return Stream.of(pos,
                new Position(pos.getColumn(), pos.getRow() - 1),
                new Position(pos.getColumn(), pos.getRow() + 1),
                left,
                right
        ).filter(x -> x != null);
    }
}
