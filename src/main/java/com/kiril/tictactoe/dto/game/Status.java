package com.kiril.tictactoe.dto.game;

/**
 * Status of game.
 *
 * @author kakshonau
 */
public enum Status {

    /** Everything is fine, game is in progress. */
    OK("State is good"),
    /** Game is finished correctly. */
    FINISHED("Game finished. We have a winner"),
    /** Game finished with errors. */
    ERROR("Unexpected error. Game is in inconsistent state");

    private final String caption;

    private Status(String caption) {
        this.caption = caption;
    }

    /**
     * Returns caption of game state.
     *
     * @return caption.
     */
    public String getCaption() {
        return caption;
    }
}
