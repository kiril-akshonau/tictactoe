package com.kiril.tictactoe.service.game;

import com.kiril.tictactoe.dto.game.FieldState;
import com.kiril.tictactoe.dto.game.Figure;
import com.kiril.tictactoe.dto.game.GameMove;
import com.kiril.tictactoe.dto.game.Status;

/**
 * Class supports game steps and game rules.
 *
 * @author kakshonau
 */
public interface GameRuleService {

    /**
     * Tries to initialize game context.
     *
     * @param figure figure to initialize. Cross for host, zero for client.
     * @return null for new game, current game state in case if game already initialized.
     */
    FieldState tryToInit(Figure figure);

    /**
     * Swap game context with incoming. Uses for reconnecting.
     *
     * @param newState new state of game.
     */
    void reInit(FieldState newState);

    /**
     * Makes first move.
     *
     * @return made move.
     */
    GameMove makeFirstMove();

    /**
     * Pushes move unconditionally. Uses for last moves when no need to make new moves.
     *
     * @param gameMove move to push into game context.
     */
    void pushMove(GameMove gameMove);

    /**
     * Pushes move from opposite side and make your own move.
     *
     * @param oldMove move from opposite side.
     * @return made move.
     */
    GameMove makeMove(GameMove oldMove);

    /**
     * Makes new move. Used for reconnecting.
     *
     * @return made move.
     */
    GameMove makeMove();

    /**
     * Switches game into error {@link Status#ERROR status}.
     */
    void markGameAsInconsistent();

}