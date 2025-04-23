package com.kiril.tictactoe.dto.event;

/**
 * Types of game events.
 *
 * @author kakshonau
 */
public enum GameEventType {

    /** Indicates that client will try to connect to host. */
    TRY_TO_CONNECT,
    /** Host received connection from client. */
    CONNECTION_RECEIVED,
    /** Send event to host application. */
    SEND_TO_HOST,
    /** Send event to client application. */
    SEND_TO_CLIENT,
    /** Indicates that it's host time to make move. */
    HOST_MOVE,
    /** Indicates that it's client time to make move. */
    CLIENT_MOVE,
    /** Appears during reconnect. Indicates that it's time to swap game field to received field. */
    CLIENT_REAPPLY_FIELD,
    /** Game was finished successfully. */
    GAME_FINISHED,
    /** Game was finished with errors. */
    STATE_INCONSISTENT

}
