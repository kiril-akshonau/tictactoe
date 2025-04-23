package com.kiril.tictactoe.exception;

/**
 * Exception for incorrect game situations.
 *
 * @author kakshonau
 */
public class GameStateException extends RuntimeException {

    public GameStateException(String message) {
        super(message);
    }

    public static GameStateException notInit() {
        return new GameStateException("Game isn't initialized");
    }
}
