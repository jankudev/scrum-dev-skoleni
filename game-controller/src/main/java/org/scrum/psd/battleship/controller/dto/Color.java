package org.scrum.psd.battleship.controller.dto;

public enum Color {
    BLUE("\u001B[34m"),
    GREEN("\u001B[32m"),
    PURPLE("\u001B[35m"),
    RED("\u001B[31m"),
    YELLOW("\u001B[33m"),
    CYAN("\u001B[36m"),
    BLACK("\u001B[30m"),
    WHITE("\u001B[37m");

    private static final String ANSI_RESET = "\u001B[0m";
    private String code;

    Color(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public String getColoredText(String text) {
        return String.format("%s%s%s", this.getCode(), text, ANSI_RESET);
    }
}
