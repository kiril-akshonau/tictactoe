package com.kiril.tictactoe.dto.game;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Contains information about current game field: state, figures, turn, etc.
 *
 * @author kakshonau
 */
public class FieldState {

    private final Figure[][] gameField;
    private Status status = null;
    private int turn = 0;

    public FieldState(Figure[][] gameField, Status status) {
        this(gameField, status, 0);
    }

    @JsonCreator
    public FieldState(@JsonProperty("gameField") Figure[][] gameField, @JsonProperty("status") Status status, @JsonProperty("turn") int turn) {
        this.gameField = gameField;
        this.status = status;
        this.turn = turn;
    }

    public Figure[][] getGameField() {
        return gameField;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public int getTurn() {
        return turn;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(gameField)
                .append(status)
                .append(turn)
                .toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj.getClass() != getClass()) {
            return false;
        }
        FieldState other = (FieldState) obj;
        return new EqualsBuilder()
                .append(gameField, other.gameField)
                .append(status, other.status)
                .append(turn, other.turn)
                .isEquals();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("gameField", gameField)
                .append("status", status)
                .append("turn", turn)
                .toString();
    }
}
