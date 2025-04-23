package com.kiril.tictactoe.service.game.impl;

import com.google.common.base.Preconditions;
import com.kiril.tictactoe.dto.game.FieldState;
import com.kiril.tictactoe.dto.game.Figure;
import com.kiril.tictactoe.dto.game.GameMove;
import com.kiril.tictactoe.dto.game.Status;
import com.kiril.tictactoe.exception.GameStateException;
import com.kiril.tictactoe.service.game.FieldOrchestrator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Implementation of {@link FieldOrchestrator}.
 *
 * @author kakshonau
 */
@Service
public class FieldOrchestratorImpl implements FieldOrchestrator {

    private static final Logger LOGGER = LoggerFactory.getLogger(FieldOrchestratorImpl.class);

    private FieldState fieldState;
    private Figure playFigure;
    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    @Override
    public FieldState tryToInit(Figure firstFigure) {
        try {
            lock.writeLock().lock();
            if (null == fieldState) {
                this.fieldState = new FieldState(new Figure[3][3], Status.OK);
                this.playFigure = firstFigure;
                return null;
            } else {
                return getFieldState();
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void reInit(FieldState newState) {
        try {
            lock.writeLock().lock();
            this.fieldState = new FieldState(copyField(newState.getGameField()), newState.getStatus(), newState.getTurn());
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public Figure[][] getGameField() {
        isInit();
        try {
            lock.readLock().lock();
            return copyField(fieldState.getGameField());
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public Figure getPlayFigure() {
        try {
            lock.readLock().lock();
            return playFigure;
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public FieldState getFieldState() {
        try {
            lock.readLock().lock();
            return new FieldState(getGameField(), fieldState.getStatus(), fieldState.getTurn());
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public boolean setFigure(GameMove move) {
        Preconditions.checkNotNull(move.figure(), "Figure can't be null");
        var figure = move.figure();
        var xPosition = move.xPosition();
        var yPosition = move.yPosition();

        try {
            lock.writeLock().lock();

            if (fieldState.getTurn() + 1 != move.turn()) {
                LOGGER.warn("Incomming turn of game is inconsistent. Have expected [{}], but have [{}]", fieldState.getTurn() + 1, move.turn());
                fieldState.setStatus(Status.ERROR);
                return false;
            }

            var gameField = fieldState.getGameField();
            if (gameField.length < yPosition + 1 || gameField[yPosition].length < xPosition + 1 || null != gameField[yPosition][xPosition]) {
                LOGGER.error("Unacceptable move [{}] x : [{}] y : [{}]", figure, xPosition, yPosition);
                fieldState.setStatus(Status.ERROR);
                return false;
            }
            gameField[yPosition][xPosition] = figure;
            fieldState.setTurn(move.turn());
        } finally {
            lock.writeLock().unlock();
        }
        return true;
    }

    @Override
    public boolean compareAndSet(Figure[][] oldGameField, GameMove oldMove, GameMove newMove) {
        try {
            lock.writeLock().lock();

            if (playFigure != oldMove.figure().opposite() || playFigure != newMove.figure()) {
                LOGGER.error("Game is in inconsistent state. Instant plays with [{}], received [{}] wanted to play with [{}]", playFigure, oldMove.figure().opposite(), newMove.figure());
                fieldState.setStatus(Status.ERROR);
                return false;
            }

            Figure[][] currentGameField = fieldState.getGameField();
            if (!Arrays.deepEquals(oldGameField, currentGameField)) {
                LOGGER.error("Game is in unconsistent state. Has field [{}], but expected [{}]", Arrays.deepToString(currentGameField), Arrays.deepToString(oldGameField));
                fieldState.setStatus(Status.ERROR);
                return false;
            }
            return setFigure(oldMove) && setFigure(newMove);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public boolean compareAndSet(Figure[][] oldGameField, GameMove move) {
        try {
            lock.writeLock().lock();

            Figure[][] currentGameField = fieldState.getGameField();
            if (!Arrays.deepEquals(oldGameField, currentGameField)) {
                LOGGER.error("Game is in unconsistent state. Has field [{}], but expected [{}]", Arrays.deepToString(currentGameField), Arrays.deepToString(oldGameField));
                fieldState.setStatus(Status.ERROR);
                return false;
            }
            return setFigure(move);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void gameDone() {
        try {
            lock.writeLock().lock();
            fieldState.setStatus(Status.FINISHED);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void gameInconsistent() {
        try {
            lock.writeLock().lock();
            fieldState.setStatus(Status.ERROR);
        } finally {
            lock.writeLock().unlock();
        }
    }

    private void isInit() {
        if (null == fieldState) {
            throw GameStateException.notInit();
        }
    }

    private Figure[][] copyField(Figure[][] field) {
        return Arrays.stream(field)
                .map(arr -> Arrays.copyOf(arr, arr.length))
                .toArray(Figure[][]::new);
    }
}
