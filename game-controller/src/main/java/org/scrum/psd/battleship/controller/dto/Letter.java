package org.scrum.psd.battleship.controller.dto;

public enum Letter {
    A(0), B(1), C(2), D(3), E(4), F(5), G(6), H(7);

    Letter(Integer index) {
        this.index = index;
    }

    public Integer getIndex() {
        return index;
    }

    public static Letter getByIndex(int index) {
        for (Letter letter : Letter.values()
        ) {
            if (letter.getIndex().equals(index))
                return letter;
        }
        throw new IllegalArgumentException();
    }

    private final Integer index;
}
