package com.kiril.tictactoe.service.event.impl.processor;

import com.kiril.tictactoe.dto.event.GameEvent;
import com.kiril.tictactoe.dto.event.GameEventType;
import com.kiril.tictactoe.service.event.EventProcessor;
import com.kiril.tictactoe.service.game.GameRuleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Implementation of {@link EventProcessor}. Handles {@link GameEventType#GAME_FINISHED} events.
 *
 * @author kakshonau
 */
@Component
public class GameFinishedProcessor implements EventProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(GameFinishedProcessor.class);

    private final GameRuleService gameRuleService;

    public GameFinishedProcessor(GameRuleService gameRuleService) {
        this.gameRuleService = gameRuleService;
    }

    @Override
    public void processEvent(GameEvent event) throws InterruptedException {
        if (null != event.gameMove()) {
            gameRuleService.pushMove(event.gameMove());
        }
        LOGGER.debug("Game is done");
    }

    @Override
    public GameEventType getEventType() {
        return GameEventType.GAME_FINISHED;
    }
}
