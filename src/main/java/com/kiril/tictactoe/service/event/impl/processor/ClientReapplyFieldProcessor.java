package com.kiril.tictactoe.service.event.impl.processor;

import com.kiril.tictactoe.dto.event.GameEvent;
import com.kiril.tictactoe.dto.event.GameEventType;
import com.kiril.tictactoe.service.event.EventOrchestrator;
import com.kiril.tictactoe.service.event.EventProcessor;
import com.kiril.tictactoe.service.game.GameRuleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Implementation of {@link EventProcessor}. Handles {@link GameEventType#CLIENT_REAPPLY_FIELD} events.
 *
 * @author kakshonau
 */
@Component
public class ClientReapplyFieldProcessor implements EventProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClientReapplyFieldProcessor.class);

    private final GameRuleService gameRuleService;
    private final EventOrchestrator eventOrchestrator;

    public ClientReapplyFieldProcessor(GameRuleService gameRuleService, EventOrchestrator eventOrchestrator) {
        this.gameRuleService = gameRuleService;
        this.eventOrchestrator = eventOrchestrator;
    }

    @Override
    public void processEvent(GameEvent event) {
        LOGGER.info("Received game field from host. Reapplying [{}]", event.fieldState());
        gameRuleService.reInit(event.fieldState());
        eventOrchestrator.fireEvent(GameEvent.withEvent(GameEventType.CLIENT_MOVE));
    }

    @Override
    public GameEventType getEventType() {
        return GameEventType.CLIENT_REAPPLY_FIELD;
    }
}
