package org.scrum.psd.battleship.controller.dto;

import java.util.ArrayList;
import java.util.List;

public class Ship {
    private boolean isPlaced;
    private String name;
    private int size;

    private List<Position> positions;

    public Ship() {
        this.positions = new ArrayList<>();
    }

    public Ship(String name, int size) {
        this();

        this.name = name;
        this.size = size;
    }

    public Ship(String name, int size, List<Position> positions) {
        this(name, size);

        this.positions = positions;
    }

    public void addPosition(Position position) {
        if (positions == null) {
            positions = new ArrayList<>();
        }
        positions.add(position);
    }

    public boolean isSunk() {
        return positions.stream().allMatch(x -> x != null && x.isHit());
    }

    public boolean hit(Position position) {
        int idx = this.positions.indexOf(position);
        if (idx >= 0) {
            this.positions.get(idx).setHit(true);
            return true;
        }
        return false;
    }

    // TODO: property change listener implementieren

    public boolean isPlaced() {
        return isPlaced;
    }

    public void setPlaced(boolean placed) {
        isPlaced = placed;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Position> getPositions() {
        return this.positions;
    }

    public void setPositions(List<Position> positions) {
        this.positions = positions;
    }


    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
