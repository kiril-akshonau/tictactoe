package com.kiril.tictactoe.service.event.impl.processor;

import com.kiril.tictactoe.dto.event.GameEvent;
import com.kiril.tictactoe.dto.event.GameEventType;
import com.kiril.tictactoe.dto.game.Figure;
import com.kiril.tictactoe.service.event.EventOrchestrator;
import com.kiril.tictactoe.service.event.EventProcessor;
import com.kiril.tictactoe.service.game.GameRuleService;
import com.kiril.tictactoe.service.screen.ScreenHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Implementation of {@link EventProcessor}. Handles {@link GameEventType#CONNECTION_RECEIVED} events.
 *
 * @author kakshonau
 */

@Component
public class ConnectionReceivedProcessor implements EventProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConnectionReceivedProcessor.class);

    private final GameRuleService gameRuleService;
    private final ScreenHandler screenHandler;
    private final EventOrchestrator eventOrchestrator;

    public ConnectionReceivedProcessor(GameRuleService gameRuleService, ScreenHandler screenHandler, EventOrchestrator eventOrchestrator) {
        this.gameRuleService = gameRuleService;
        this.screenHandler = screenHandler;
        this.eventOrchestrator = eventOrchestrator;
    }

    @Override
    public void processEvent(GameEvent event) {
        LOGGER.info("Received new connection. Starting game.....");

        var currentFieldState = gameRuleService.tryToInit(Figure.CROSS);
        if (null == currentFieldState) {
            screenHandler.startDraw();
            var gameMove = gameRuleService.makeFirstMove();
            if (null != gameMove) {
                eventOrchestrator.fireEvent(GameEvent.withMove(GameEventType.SEND_TO_CLIENT, gameMove));
            } else {
                LOGGER.error("Unexpected exception. Can't make first move");
                eventOrchestrator.fireEvent(GameEvent.withEvent(GameEventType.STATE_INCONSISTENT));
            }
        } else {
            eventOrchestrator.fireEvent(GameEvent.withFieldState(GameEventType.SEND_TO_CLIENT, currentFieldState));
        }
    }

    @Override
    public GameEventType getEventType() {
        return GameEventType.CONNECTION_RECEIVED;
    }

}
