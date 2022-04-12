package org.scrum.psd.battleship.controller.dto;

import java.util.Objects;

public class Position {
    private Letter column;
    private int row;

    private boolean hit; //todo extract to ShipParts

    public Position(Letter column, int row) {
        Objects.requireNonNull(column);
        this.column = column;
        this.row = row;
    }

    public Letter getColumn() {
        return column;
    }

    public void setColumn(Letter column) {
        this.column = column;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public boolean isHit() {
        return hit;
    }

    public void setHit(boolean hit) {
        this.hit = hit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return row == position.row && column == position.column;
    }

    @Override
    public int hashCode() {
        return Objects.hash(column, row);
    }
}
