package org.scrum.psd.battleship.controller.dto;

public enum CellState {
    UNKNOWN(Color.WHITE.getColoredText(".")),
    HIT(Color.RED.getColoredText("x")),
    MISS(Color.BLUE.getColoredText("~"));

    private String printSymbol;

    CellState(String symbol) {
        this.printSymbol = symbol;
    }

    public String getPrintSymbol() {
        return printSymbol;
    }
}
