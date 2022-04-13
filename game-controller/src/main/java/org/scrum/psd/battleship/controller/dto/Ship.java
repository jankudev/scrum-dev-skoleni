package org.scrum.psd.battleship.controller.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Ship {
    private String name;
    private int size;

    private List<ShipPart> shipParts;

    public Ship() {
        this.shipParts = new ArrayList<>();
    }

    public Ship(String name, int size) {
        this();

        this.name = name;
        this.size = size;
    }

    public Ship(String name, int size, List<Position> positions) {
        this(name, size);

        this.shipParts = positions.stream().map(p -> new ShipPart(p)).collect(Collectors.toList());
    }

    public void addShipPart(String input) {
        Letter letter = Letter.valueOf(input.toUpperCase().substring(0, 1));
        int number = Integer.parseInt(input.substring(1));

        addShipPart(new Position(letter, number));
    }

    public void addShipPart(Position position) {
        if (shipParts == null) {
            shipParts = new ArrayList<>();
        }
        shipParts.add(new ShipPart(position));
    }

    public boolean isSunk() {
        return shipParts.stream().allMatch(x -> x != null && x.isHit());
    }

    public boolean hit(Position position) {
        final Optional<ShipPart> hittedPart = this.shipParts.stream().filter(part -> position.equals(part.getPosition())).findFirst();
        if (hittedPart.isPresent()) {
            hittedPart.get().markHit();
            return true;
        }
        return false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public List<ShipPart> getShipParts() {
        return shipParts;
    }
}
