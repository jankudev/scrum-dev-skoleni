package org.scrum.psd.battleship.controller.dto;

public class ShipPosition {
    private final Position position;
    private final boolean isVertical;

    public ShipPosition(Position position, boolean isVertical) {
        this.position = position;
        this.isVertical = isVertical;
    }

    public Position getPosition() {
        return position;
    }

    public boolean isVertical() {
        return isVertical;
    }
}
