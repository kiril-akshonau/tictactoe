package com.kiril.tictactoe.service.event.impl.processor;

import com.kiril.tictactoe.dto.event.GameEvent;
import com.kiril.tictactoe.dto.event.GameEventType;
import com.kiril.tictactoe.service.event.EventProcessor;
import com.kiril.tictactoe.service.game.GameRuleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Implementation of {@link EventProcessor}. Handles {@link GameEventType#STATE_INCONSISTENT} events.
 *
 * @author kakshonau
 */
@Component
public class GameInconsistentProcessor implements EventProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(GameInconsistentProcessor.class);

    private final GameRuleService gameRuleService;

    public GameInconsistentProcessor(GameRuleService gameRuleService) {
        this.gameRuleService = gameRuleService;
    }

    @Override
    public void processEvent(GameEvent event) throws InterruptedException {
        gameRuleService.markGameAsInconsistent();
        LOGGER.debug("Game is inconsistent");
    }

    @Override
    public GameEventType getEventType() {
        return GameEventType.STATE_INCONSISTENT;
    }

}
