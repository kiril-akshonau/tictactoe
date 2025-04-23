package com.kiril.tictactoe.dto.event;

import com.kiril.tictactoe.dto.game.FieldState;
import com.kiril.tictactoe.dto.game.GameMove;

/**
 * Class contains context of game events.
 *
 * @author kakshonau
 */
public record GameEvent(GameEventType eventType, GameMove gameMove, FieldState fieldState, int port) {

    public static GameEvent withEvent(GameEventType eventType) {
        return withMove(eventType, null);
    }

    public static GameEvent withMove(GameEventType eventType, GameMove gameMove) {
        return new GameEvent(eventType, gameMove, null, -1);
    }

    public static GameEvent withPort(GameEventType eventType, int port) {
        return new GameEvent(eventType, null, null, port);
    }

    public static GameEvent withFieldState(GameEventType eventType, FieldState fieldState) {
        return new GameEvent(eventType, null, fieldState, -1);
    }
}
