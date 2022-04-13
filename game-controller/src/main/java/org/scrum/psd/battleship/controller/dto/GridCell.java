package org.scrum.psd.battleship.controller.dto;

/**
 * Grid history to remember the moves and show player the current state of his attempts
 */
public class GridCell {
    public Position position;
    public CellState state;

    public GridCell(Position position, CellState state) {
        this.position = position;
        this.state = state;
    }
}
