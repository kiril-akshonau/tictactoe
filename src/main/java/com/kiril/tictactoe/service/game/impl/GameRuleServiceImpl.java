package com.kiril.tictactoe.service.game.impl;

import com.kiril.tictactoe.dto.game.FieldState;
import com.kiril.tictactoe.dto.game.Figure;
import com.kiril.tictactoe.dto.game.GameMove;
import com.kiril.tictactoe.dto.game.Status;
import com.kiril.tictactoe.service.game.FieldOrchestrator;
import com.kiril.tictactoe.service.game.GameRuleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Implementation of {@link GameRuleService}.
 *
 * @author kakshonau
 */
@Service
public class GameRuleServiceImpl implements GameRuleService {

    private static final Logger LOGGER = LoggerFactory.getLogger(GameRuleServiceImpl.class);
    private static final String FULL_FIELD_MESSAGE = "No more empty slots. Game is finished";

    private final FieldOrchestrator fieldOrchestrator;
    private final Random random = new Random();

    public GameRuleServiceImpl(FieldOrchestrator fieldOrchestrator) {
        this.fieldOrchestrator = fieldOrchestrator;
    }

    @Override
    public FieldState tryToInit(Figure figure) {
        return fieldOrchestrator.tryToInit(figure);
    }

    @Override
    public void reInit(FieldState newState) {
        fieldOrchestrator.reInit(newState);
    }

    @Override
    public GameMove makeFirstMove() {
        var firstPosition = random.nextInt(9);
        var yPosition = firstPosition / 3;
        var xPosition = firstPosition % 3;
        var gameMove = new GameMove(Figure.CROSS, xPosition, yPosition, 1, Status.OK);
        return fieldOrchestrator.setFigure(gameMove) ? gameMove : null;
    }

    @Override
    public void pushMove(GameMove gameMove) {
        if (!fieldOrchestrator.setFigure(gameMove)) {
            LOGGER.warn("Inconcistent move [{}]", gameMove);
            return;
        }
        if (checkWinner()) {
            LOGGER.info("We have a winner");
            fieldOrchestrator.gameDone();
        }
    }

    @Override
    public GameMove makeMove(GameMove oldMove) {
        var gameField = fieldOrchestrator.getGameField();
        var emptySlots = chooseEmptySlots(gameField, oldMove);

        if (emptySlots.isEmpty()) {
            LOGGER.warn(FULL_FIELD_MESSAGE);
            fieldOrchestrator.gameDone();
            return new GameMove(null, -1, -1, -1, Status.FINISHED);
        }

        var newPosition = emptySlots.get(random.nextInt(emptySlots.size()));
        var yPosition = newPosition / 3;
        var xPosition = newPosition % 3;
        var newMove = new GameMove(oldMove.figure().opposite(), xPosition, yPosition, oldMove.turn() +1, null);
        var status = calculateStatus(emptySlots, fieldOrchestrator.compareAndSet(gameField, oldMove, newMove));

        return new GameMove(newMove.figure(), xPosition, yPosition, newMove.turn(), status);
    }

    @Override
    public GameMove makeMove() {
        var gameState = fieldOrchestrator.getFieldState();
        var gameField = gameState.getGameField();
        var figure = fieldOrchestrator.getPlayFigure();
        var emptySlots = chooseEmptySlots(gameField, null);

        if (emptySlots.isEmpty()) {
            LOGGER.warn(FULL_FIELD_MESSAGE);
            fieldOrchestrator.gameDone();
            return new GameMove(null, -1, -1, -1, Status.FINISHED);
        }

        var newPosition = emptySlots.get(random.nextInt(emptySlots.size()));
        var yPosition = newPosition / 3;
        var xPosition = newPosition % 3;
        var newMove = new GameMove(figure, xPosition, yPosition, gameState.getTurn() + 1, null);
        var status = calculateStatus(emptySlots, fieldOrchestrator.compareAndSet(gameField, newMove));

        return new GameMove(newMove.figure(), xPosition, yPosition, gameState.getTurn() + 1, status);
    }

    @Override
    public void markGameAsInconsistent() {
        fieldOrchestrator.gameInconsistent();
    }

    private Status calculateStatus(List<Integer> emptySlots, boolean result) {
        var status = result ? Status.OK : Status.ERROR;

        if (emptySlots.size() == 1) {
            LOGGER.warn(FULL_FIELD_MESSAGE);
            status = Status.FINISHED;
        }
        if (checkWinner()) {
            LOGGER.info("We have a winner");
            status = Status.FINISHED;
            fieldOrchestrator.gameDone();
        }
        return status;
    }

    private List<Integer> chooseEmptySlots(Figure[][] gameField, GameMove oldMove) {
        var emptySlots = new ArrayList<Integer>(9);
        for (int i = 0; i < gameField.length; i++) {
            for (int j = 0; j < gameField[i].length; j++) {
                if (null == gameField[i][j] && !(null != oldMove && j == oldMove.xPosition() && i == oldMove.yPosition())) {
                    emptySlots.add(i * 3 + j);
                }
            }
        }
        return emptySlots;
    }

    private boolean checkWinner() {
        var gameField = fieldOrchestrator.getGameField();

        for (int i = 0; i < gameField.length; i++) {
            if (gameField[i][0] == gameField[i][1] && gameField[i][1] == gameField[i][2] && null != gameField[i][0] && null != gameField[i][1] && null != gameField[i][2]) {
                return true;
            }
        }

        for (int i = 0; i < gameField.length; i++) {
            if (gameField[0][i] == gameField[1][i] && gameField[1][i] == gameField[2][i] && null != gameField[0][i] && null != gameField[1][i] && null != gameField[2][i]) {
                return true;
            }
        }

        if (gameField[0][0] == gameField[1][1] && gameField[1][1] == gameField[2][2] && null != gameField[0][0] && null != gameField[1][1] && null != gameField[2][2]) {
            return true;
        }

        return gameField[0][2] == gameField[1][1] && gameField[1][1] == gameField[2][0] && null != gameField[0][2] && null != gameField[1][1] && null != gameField[2][0];
    }
}
