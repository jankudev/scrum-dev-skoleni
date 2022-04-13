package org.scrum.psd.battleship.controller;

import org.scrum.psd.battleship.controller.dto.Position;

import java.util.List;

public class Validator {

    static boolean isValidShipPlacement(List<Position> generatedFields, List<Position> alreadyTakenPositions) {
        if (isThereConflict(generatedFields, alreadyTakenPositions)) {
            return false;
        }

        return true;
    }

    private static boolean isThereConflict(List<Position> generatedFields, List<Position> alreadyTakenPositions) {
        return generatedFields.stream().anyMatch(alreadyTakenPositions::contains);
    }
}
