package com.kiril.tictactoe.exception;

/**
 * Exception for situations with connections.
 *
 * @author kakshonau
 */
public class GameConnectionException extends Exception {

    public GameConnectionException(String message, Exception root) {
        super(message, root);
    }
}
