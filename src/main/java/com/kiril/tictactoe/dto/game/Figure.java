package com.kiril.tictactoe.dto.game;

/**
 * Figures.
 *
 * @author kakshonau
 */
public enum Figure {

    CROSS("X"),
    ZERO("0");

    private final String character;

    private Figure(String character) {
        this.character = character;
    }

    public String getCharacter() {
        return character;
    }

    /**
     * Gets opposite figure.
     *
     * @return opposite figure.
     */
    public Figure opposite() {
        return this == CROSS ? ZERO : CROSS;
    }
}
