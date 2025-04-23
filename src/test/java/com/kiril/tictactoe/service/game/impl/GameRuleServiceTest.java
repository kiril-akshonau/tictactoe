package com.kiril.tictactoe.service.game.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.kiril.tictactoe.dto.game.FieldState;
import com.kiril.tictactoe.dto.game.Figure;
import com.kiril.tictactoe.dto.game.GameMove;
import com.kiril.tictactoe.dto.game.Status;
import com.kiril.tictactoe.service.game.FieldOrchestrator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class GameRuleServiceTest {

    @Mock
    private FieldOrchestrator fieldOrchestrator;

    @InjectMocks
    private GameRuleServiceImpl gameRuleService;

    @Test
    void testTryToInit() {
        when(fieldOrchestrator.tryToInit(Figure.CROSS)).thenReturn(null);

        gameRuleService.tryToInit(Figure.CROSS);

        verify(fieldOrchestrator).tryToInit(Figure.CROSS);
    }

    @Test
    void testReInit() {
        var state = new FieldState(null, Status.ERROR);

        gameRuleService.reInit(state);

        verify(fieldOrchestrator).reInit(state);
    }

    @Test
    void testMakeFirstMoveIncorrectMove() {
        when(fieldOrchestrator.setFigure(any(GameMove.class))).thenReturn(false);

        assertThat(gameRuleService.makeFirstMove()).isNull();

        verify(fieldOrchestrator).setFigure(any(GameMove.class));
    }

    @Test
    void testMakeFirstMove() {
        when(fieldOrchestrator.setFigure(any(GameMove.class))).thenReturn(true);

        var firstMove = gameRuleService.makeFirstMove();

        assertThat(firstMove).isNotNull();
        assertThat(firstMove.figure()).isEqualTo(Figure.CROSS);
        assertThat(firstMove.status()).isEqualTo(Status.OK);
        assertThat(firstMove.turn()).isEqualTo(1);
        assertThat(firstMove.xPosition()).isGreaterThanOrEqualTo(0);
        assertThat(firstMove.yPosition()).isGreaterThanOrEqualTo(0);
        verify(fieldOrchestrator).setFigure(any(GameMove.class));
    }

    @Test
    void testPushMoveIncorrectMove() {
        var move = new GameMove(Figure.CROSS, 0, 0, 0, null);
        when(fieldOrchestrator.setFigure(move)).thenReturn(false);

        gameRuleService.pushMove(move);

        verify(fieldOrchestrator, never()).getGameField();
        verify(fieldOrchestrator, never()).gameDone();
    }

    @Test
    void testPushMoveNoWinner() {
        var move = new GameMove(Figure.CROSS, 0, 0, 0, null);
        var field = new Figure[3][3];
        when(fieldOrchestrator.setFigure(move)).thenReturn(true);
        when(fieldOrchestrator.getGameField()).thenReturn(field);

        gameRuleService.pushMove(move);

        verify(fieldOrchestrator).getGameField();
        verify(fieldOrchestrator, never()).gameDone();
    }

    @Test
    void testPushMoveWithWinner() {
        var move = new GameMove(Figure.CROSS, 0, 0, 0, null);
        var field = new Figure[3][3];
        field[0][0] = Figure.CROSS;
        field[0][1] = Figure.CROSS;
        field[0][2] = Figure.CROSS;
        when(fieldOrchestrator.setFigure(move)).thenReturn(true);
        when(fieldOrchestrator.getGameField()).thenReturn(field);

        gameRuleService.pushMove(move);

        verify(fieldOrchestrator).getGameField();
        verify(fieldOrchestrator).gameDone();
    }

    @Test
    void testMakeMoveFullField() {
        var move = new GameMove(Figure.CROSS, 0, 0, 0, null);
        var field = new Figure[1][1];
        field[0][0] = Figure.CROSS;
        when(fieldOrchestrator.getGameField()).thenReturn(field);

        var resultMove = gameRuleService.makeMove(move);

        assertThat(resultMove.status()).isEqualTo(Status.FINISHED);
        verify(fieldOrchestrator).getGameField();
        verify(fieldOrchestrator).gameDone();
    }

    @Test
    void testMakeMove() {
        var move = new GameMove(Figure.CROSS, 0, 0, 1, null);
        var field = new Figure[3][3];
        when(fieldOrchestrator.getGameField()).thenReturn(field);
        when(fieldOrchestrator.compareAndSet(eq(field), eq(move), any(GameMove.class))).thenReturn(true);

        var resultMove = gameRuleService.makeMove(move);

        assertThat(resultMove).isNotNull();
        assertThat(resultMove.figure()).isEqualTo(Figure.ZERO);
        assertThat(resultMove.status()).isEqualTo(Status.OK);
        assertThat(resultMove.turn()).isEqualTo(2);
        assertThat(resultMove.xPosition()).isGreaterThanOrEqualTo(0);
        assertThat(resultMove.yPosition()).isGreaterThanOrEqualTo(0);
        verify(fieldOrchestrator, times(2)).getGameField();
        verify(fieldOrchestrator).compareAndSet(eq(field), eq(move), any(GameMove.class));
        verify(fieldOrchestrator, never()).gameDone();
    }

    @Test
    void testMakeMoveWithoutOldMoveFullField() {
        var field = new Figure[1][1];
        field[0][0] = Figure.CROSS;
        when(fieldOrchestrator.getFieldState()).thenReturn(new FieldState(field, Status.OK, 0));

        var resultMove = gameRuleService.makeMove();

        assertThat(resultMove.status()).isEqualTo(Status.FINISHED);
        verify(fieldOrchestrator).getFieldState();
        verify(fieldOrchestrator).gameDone();
    }

    @Test
    void testMakeMoveWithoutOldMoveLastMove() {
        var field = new Figure[3][3];
        field[0][0] = Figure.CROSS;
        field[0][1] = Figure.CROSS;
        field[0][2] = Figure.ZERO;
        field[1][0] = Figure.CROSS;
        field[1][1] = Figure.CROSS;
        field[1][2] = Figure.ZERO;
        field[2][0] = Figure.ZERO;
        field[2][1] = Figure.CROSS;
        when(fieldOrchestrator.getFieldState()).thenReturn(new FieldState(field, Status.OK, 0));
        when(fieldOrchestrator.getGameField()).thenReturn(field);

        var resultMove = gameRuleService.makeMove();

        assertThat(resultMove.status()).isEqualTo(Status.FINISHED);
        verify(fieldOrchestrator).getGameField();
        verify(fieldOrchestrator).getFieldState();
        verify(fieldOrchestrator).gameDone();
    }

    @Test
    void testMakeMoveWithoutOldMove() {
        var field = new Figure[3][3];
        field[1][0] = Figure.CROSS;
        field[1][1] = Figure.CROSS;
        field[1][2] = Figure.CROSS;
        when(fieldOrchestrator.getGameField()).thenReturn(field);
        when(fieldOrchestrator.getFieldState()).thenReturn(new FieldState(field, Status.OK, 1));
        when(fieldOrchestrator.compareAndSet(eq(field), any(GameMove.class))).thenReturn(true);
        when(fieldOrchestrator.getPlayFigure()).thenReturn(Figure.ZERO);

        var resultMove = gameRuleService.makeMove();

        assertThat(resultMove).isNotNull();
        assertThat(resultMove.figure()).isEqualTo(Figure.ZERO);
        assertThat(resultMove.status()).isEqualTo(Status.FINISHED);
        assertThat(resultMove.turn()).isEqualTo(2);
        assertThat(resultMove.xPosition()).isGreaterThanOrEqualTo(0);
        assertThat(resultMove.yPosition()).isGreaterThanOrEqualTo(0);
        verify(fieldOrchestrator).getPlayFigure();
        verify(fieldOrchestrator).getGameField();
        verify(fieldOrchestrator).getFieldState();
        verify(fieldOrchestrator).compareAndSet(eq(field), any(GameMove.class));
        verify(fieldOrchestrator).gameDone();
    }

    @Test
    void testMarkGameAsInconsistent() {
        gameRuleService.markGameAsInconsistent();

        verify(fieldOrchestrator).gameInconsistent();
    }

    @Test
    void testCheckWinnerVertical() {
        var field = new Figure[3][3];
        field[1][0] = Figure.CROSS;
        field[1][1] = Figure.CROSS;
        field[1][2] = Figure.CROSS;
        when(fieldOrchestrator.getGameField()).thenReturn(field);

        assertThat(ReflectionTestUtils.<Boolean> invokeMethod(gameRuleService, "checkWinner")).isTrue();

        verify(fieldOrchestrator).getGameField();
    }

    @Test
    void testCheckWinnerHorisontal() {
        var field = new Figure[3][3];
        field[0][1] = Figure.CROSS;
        field[1][1] = Figure.CROSS;
        field[2][1] = Figure.CROSS;
        when(fieldOrchestrator.getGameField()).thenReturn(field);

        assertThat(ReflectionTestUtils.<Boolean> invokeMethod(gameRuleService, "checkWinner")).isTrue();

        verify(fieldOrchestrator).getGameField();
    }

    @Test
    void testCheckWinnerDiagonal() {
        var field1 = new Figure[3][3];
        field1[0][0] = Figure.CROSS;
        field1[1][1] = Figure.CROSS;
        field1[2][2] = Figure.CROSS;
        var field2 = new Figure[3][3];
        field2[0][2] = Figure.CROSS;
        field2[1][1] = Figure.CROSS;
        field2[2][0] = Figure.CROSS;
        when(fieldOrchestrator.getGameField()).thenReturn(field1).thenReturn(field2);

        assertThat(ReflectionTestUtils.<Boolean> invokeMethod(gameRuleService, "checkWinner")).isTrue();
        assertThat(ReflectionTestUtils.<Boolean> invokeMethod(gameRuleService, "checkWinner")).isTrue();

        verify(fieldOrchestrator, times(2)).getGameField();
    }
}
