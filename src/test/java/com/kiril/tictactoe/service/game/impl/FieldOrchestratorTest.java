package com.kiril.tictactoe.service.game.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.kiril.tictactoe.dto.game.FieldState;
import com.kiril.tictactoe.dto.game.Figure;
import com.kiril.tictactoe.dto.game.GameMove;
import com.kiril.tictactoe.dto.game.Status;
import com.kiril.tictactoe.exception.GameStateException;
import com.kiril.tictactoe.service.game.FieldOrchestrator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FieldOrchestratorTest {

    private FieldOrchestrator fieldOrchestrator;

    @BeforeEach
    void setUp() {
        fieldOrchestrator = new FieldOrchestratorImpl();
    }

    @Test
    void testTryToInitEmptyField() {
        assertThat(fieldOrchestrator.tryToInit(Figure.CROSS)).isNull();

        assertThat(fieldOrchestrator.getGameField()).isNotNull();
        assertThat(fieldOrchestrator.getPlayFigure()).isEqualTo(Figure.CROSS);
        assertThat(fieldOrchestrator.getFieldState().getStatus()).isEqualTo(Status.OK);
    }

    @Test
    void testTryToInitNotEmptyField() {
        assertThat(fieldOrchestrator.tryToInit(Figure.CROSS)).isNull();
        fieldOrchestrator.setFigure(new GameMove(Figure.CROSS, 0, 0, 1, null));
        assertThat(fieldOrchestrator.tryToInit(Figure.CROSS)).isNotNull();

        assertThat(fieldOrchestrator.getGameField()).isNotNull();
        assertThat(fieldOrchestrator.getPlayFigure()).isEqualTo(Figure.CROSS);
        assertThat(fieldOrchestrator.getFieldState().getStatus()).isEqualTo(Status.OK);
    }

    @Test
    void testGetGameFieldNotInitedField() {
        assertThatThrownBy(() -> fieldOrchestrator.getGameField()).isInstanceOf(GameStateException.class);
    }

    @Test
    void testReInit() {
        fieldOrchestrator.tryToInit(Figure.CROSS);

        var fieldState = new FieldState(new Figure[5][5], Status.ERROR, 42);
        fieldOrchestrator.reInit(fieldState);

        assertThat(fieldOrchestrator.getFieldState()).isEqualTo(fieldState);
    }

    @Test
    void testSetFigureInconsistentTurn() {
        fieldOrchestrator.tryToInit(Figure.CROSS);

        assertThat(fieldOrchestrator.setFigure(new GameMove(Figure.CROSS, 0, 0, -1, null))).isFalse();

        assertThat(fieldOrchestrator.getFieldState().getStatus()).isEqualTo(Status.ERROR);
    }

    @Test
    void testSetFigureInconsistentXCoordinate() {
        fieldOrchestrator.tryToInit(Figure.CROSS);

        assertThat(fieldOrchestrator.setFigure(new GameMove(Figure.CROSS, 42, 0, 1, null))).isFalse();

        assertThat(fieldOrchestrator.getFieldState().getStatus()).isEqualTo(Status.ERROR);
    }

    @Test
    void testSetFigureInconsistentYCoordinate() {
        fieldOrchestrator.tryToInit(Figure.CROSS);

        assertThat(fieldOrchestrator.setFigure(new GameMove(Figure.CROSS, 0, 42, 1, null))).isFalse();

        assertThat(fieldOrchestrator.getFieldState().getStatus()).isEqualTo(Status.ERROR);
    }

    @Test
    void testSetFigureAlreadySetToThisCoordinate() {
        fieldOrchestrator.tryToInit(Figure.CROSS);

        assertThat(fieldOrchestrator.setFigure(new GameMove(Figure.CROSS, 0, 0, 1, null))).isTrue();
        assertThat(fieldOrchestrator.setFigure(new GameMove(Figure.CROSS, 0, 0, 2, null))).isFalse();


        assertThat(fieldOrchestrator.getFieldState().getStatus()).isEqualTo(Status.ERROR);
    }

    @Test
    void testSetFigure() {
        fieldOrchestrator.tryToInit(Figure.CROSS);

        assertThat(fieldOrchestrator.setFigure(new GameMove(Figure.CROSS, 0, 1, 1, null))).isTrue();

        assertThat(fieldOrchestrator.getGameField()[1][0]).isEqualTo(Figure.CROSS);
        assertThat(fieldOrchestrator.getFieldState().getStatus()).isEqualTo(Status.OK);
    }

    @Test
    void testCompareAndSetIncorrectOldFigure() {
        fieldOrchestrator.tryToInit(Figure.CROSS);
        var currentField = fieldOrchestrator.getGameField();

        assertThat(fieldOrchestrator.compareAndSet(currentField, new GameMove(Figure.CROSS, 0, 1, 1, null), new GameMove(Figure.CROSS, 0, 1, 1, null))).isFalse();

        assertThat(fieldOrchestrator.getFieldState().getStatus()).isEqualTo(Status.ERROR);
    }

    @Test
    void testCompareAndSetIncorrectNewFigure() {
        fieldOrchestrator.tryToInit(Figure.CROSS);
        var currentField = fieldOrchestrator.getGameField();

        assertThat(fieldOrchestrator.compareAndSet(currentField, new GameMove(Figure.ZERO, 0, 1, 1, null), new GameMove(Figure.ZERO, 0, 1, 1, null))).isFalse();

        assertThat(fieldOrchestrator.getFieldState().getStatus()).isEqualTo(Status.ERROR);
    }

    @Test
    void testCompareAndSetFieldChanged() {
        fieldOrchestrator.tryToInit(Figure.CROSS);
        var currentField = fieldOrchestrator.getGameField();
        currentField[0][0] = Figure.CROSS;

        assertThat(fieldOrchestrator.compareAndSet(currentField, new GameMove(Figure.ZERO, 0, 1, 1, null), new GameMove(Figure.CROSS, 0, 1, 1, null))).isFalse();

        assertThat(fieldOrchestrator.getFieldState().getStatus()).isEqualTo(Status.ERROR);
    }

    @Test
    void testCompareAndSetCantSetOldFigure() {
        fieldOrchestrator.tryToInit(Figure.CROSS);
        var currentField = fieldOrchestrator.getGameField();

        assertThat(fieldOrchestrator.compareAndSet(currentField, new GameMove(Figure.ZERO, 0, 10, 1, null), new GameMove(Figure.CROSS, 0, 1, 1, null))).isFalse();

        assertThat(fieldOrchestrator.getFieldState().getStatus()).isEqualTo(Status.ERROR);
    }

    @Test
    void testCompareAndSetCantSetNewFigure() {
        fieldOrchestrator.tryToInit(Figure.CROSS);
        var currentField = fieldOrchestrator.getGameField();

        assertThat(fieldOrchestrator.compareAndSet(currentField, new GameMove(Figure.ZERO, 0, 1, 1, null), new GameMove(Figure.CROSS, 0, 10, 1, null))).isFalse();

        assertThat(fieldOrchestrator.getFieldState().getStatus()).isEqualTo(Status.ERROR);
    }

    @Test
    void testCompareAndSet() {
        fieldOrchestrator.tryToInit(Figure.CROSS);
        var currentField = fieldOrchestrator.getGameField();

        assertThat(fieldOrchestrator.compareAndSet(currentField, new GameMove(Figure.ZERO, 0, 1, 1, null), new GameMove(Figure.CROSS, 0, 2, 2, null))).isTrue();

        assertThat(fieldOrchestrator.getGameField()[1][0]).isEqualTo(Figure.ZERO);
        assertThat(fieldOrchestrator.getGameField()[2][0]).isEqualTo(Figure.CROSS);
        assertThat(fieldOrchestrator.getFieldState().getStatus()).isEqualTo(Status.OK);
    }

    @Test
    void testCompareAndSetWithoutOldFieldChanged() {
        fieldOrchestrator.tryToInit(Figure.CROSS);
        var currentField = fieldOrchestrator.getGameField();
        currentField[0][0] = Figure.CROSS;

        assertThat(fieldOrchestrator.compareAndSet(currentField, new GameMove(Figure.ZERO, 0, 1, 1, null))).isFalse();

        assertThat(fieldOrchestrator.getFieldState().getStatus()).isEqualTo(Status.ERROR);
    }

    @Test
    void testCompareAndSetWithoutOldCantSetNewFigure() {
        fieldOrchestrator.tryToInit(Figure.CROSS);
        var currentField = fieldOrchestrator.getGameField();

        assertThat(fieldOrchestrator.compareAndSet(currentField, new GameMove(Figure.CROSS, 0, 10, 1, null))).isFalse();

        assertThat(fieldOrchestrator.getFieldState().getStatus()).isEqualTo(Status.ERROR);
    }

    @Test
    void testCompareAndSetWithoutOld() {
        fieldOrchestrator.tryToInit(Figure.CROSS);
        var currentField = fieldOrchestrator.getGameField();

        assertThat(fieldOrchestrator.compareAndSet(currentField, new GameMove(Figure.CROSS, 0, 2, 1, null))).isTrue();

        assertThat(fieldOrchestrator.getGameField()[2][0]).isEqualTo(Figure.CROSS);
        assertThat(fieldOrchestrator.getFieldState().getStatus()).isEqualTo(Status.OK);
    }

    @Test
    void testGameDone() {
        fieldOrchestrator.tryToInit(Figure.CROSS);
        fieldOrchestrator.gameDone();

        assertThat(fieldOrchestrator.getFieldState().getStatus()).isEqualTo(Status.FINISHED);
    }

    @Test
    void testGameInconsistent() {
        fieldOrchestrator.tryToInit(Figure.CROSS);
        fieldOrchestrator.gameInconsistent();

        assertThat(fieldOrchestrator.getFieldState().getStatus()).isEqualTo(Status.ERROR);
    }
}
