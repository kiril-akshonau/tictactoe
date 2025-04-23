package com.kiril.tictactoe.controller;

import com.kiril.tictactoe.dto.game.Figure;
import com.kiril.tictactoe.service.game.FieldOrchestrator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Rest controller to request game field.
 *
 * @author kakshonau
 */
@RestController
@RequestMapping("/state")
public class GameStateRestController {

    private final FieldOrchestrator gameOrchestrator;

    public GameStateRestController(FieldOrchestrator gameOrchestrator) {
        this.gameOrchestrator = gameOrchestrator;
    }

    /**
     * Returns game field.
     *
     * @return game field.
     */
    @GetMapping
    public Figure[][] getGameState() {
        return gameOrchestrator.getGameField();
    }
}
