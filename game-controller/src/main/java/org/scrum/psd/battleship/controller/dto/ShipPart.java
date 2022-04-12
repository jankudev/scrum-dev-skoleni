package org.scrum.psd.battleship.controller.dto;

public class ShipPart {
    private Position position;
    private boolean hit = false;

    public ShipPart(Position position) {
        this.position = position;
    }

    public void markHit() {
        this.hit = true;
    }

    public Position getPosition() {
        return position;
    }

    public boolean isHit() {
        return hit;
    }
}
