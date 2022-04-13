package org.scrum.psd.battleship.controller.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GameGridHistory {
    private List<GridCell> cells;

    public GameGridHistory() {
        cells = new ArrayList<>();
        for (Letter l : Letter.values()) {
            for (int i = 1; i < 9; i++) {
                cells.add(new GridCell(new Position(l, i), CellState.UNKNOWN));
            }
        }
    }

    public void markState(Position position, CellState state) {
        System.out.println("debug - position - " + position.getColumn().name() + position.getRow());
        cells.stream().filter(cell -> cell.position.equals(position)).findFirst().get().state = state;
    }

    public String getRowAsString(int row) {
        return cells.stream().filter(cell -> cell.position.getRow() == row)
                .map(cell -> cell.state.getPrintSymbol())
                .collect(Collectors.joining(" "));
    }
}
