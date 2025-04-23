package com.kiril.tictactoe.exception;

/**
 * Exception for unknown event loop events.
 *
 * @author kakshonau
 */
public class UnknownCommandException extends RuntimeException {

    public UnknownCommandException(String message) {
        super(message);
    }
}
