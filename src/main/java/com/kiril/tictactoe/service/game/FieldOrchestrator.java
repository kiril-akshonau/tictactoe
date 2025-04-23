package com.kiril.tictactoe.service.game;

import com.kiril.tictactoe.dto.game.FieldState;
import com.kiril.tictactoe.dto.game.Figure;
import com.kiril.tictactoe.dto.game.GameMove;
import com.kiril.tictactoe.dto.game.Status;

/**
 * Class supports inner thread safe logic of game.
 *
 * @author kakshonau
 */
public interface FieldOrchestrator {

    /**
     * Returns copy of game.
     *
     * @return copy of current game.
     */
    Figure[][] getGameField();

    /**
     * Copy of full game state.
     *
     * @return current game state.
     */
    FieldState getFieldState();

    /**
     * Sets figure into game.
     *
     * @param move move to set.
     * @return true if move was possible, false otherwise.
     */
    boolean setFigure(GameMove move);

    /**
     * Tries to initialize game context.
     *
     * @param figure figure to initialize. Cross for host, zero for client.
     * @return null for new game, current game state in case if game already initialized.
     */
    FieldState tryToInit(Figure firstFigure);

    /**
     * Compares current game state with provided and tries to set moves.
     *
     * @param oldGameField game field to synchronize. Checks that nobody made new moves.
     * @param oldMove move from opposite side.
     * @param newMove move of current side.
     * @return true if moves was possible, false otherwise.
     */
    boolean compareAndSet(Figure[][] oldGameField, GameMove oldMove, GameMove newMove);

    /**
     * Compares current game state with provided and tries to set new move.
     *
     * @param oldGameField game field to synchronize. Checks that nobody made new moves.
     * @param move move of current side.
     * @return true if moves was possible, false otherwise.
     */
    boolean compareAndSet(Figure[][] oldGameField, GameMove move);

    /**
     * Switches game into finished {@link Status#FINISHED status}.
     */
    void gameDone();

    /**
     * Swap game context with incoming. Uses for reconnecting.
     *
     * @param newState new state of game.
     */
    void reInit(FieldState newState);

    /**
     * Gets current side playable figure.
     *
     * @return current side playable figure.
     */
    Figure getPlayFigure();

    /**
     * Switches game into error {@link Status#ERROR status}.
     */
    void gameInconsistent();

}