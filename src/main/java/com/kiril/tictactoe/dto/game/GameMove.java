package com.kiril.tictactoe.dto.game;

/**
 * Contains information about game move: figure, positions, etc.
 *
 * @author kakshonau
 */
public record GameMove(Figure figure, int xPosition, int yPosition, int turn, Status status) {

}
