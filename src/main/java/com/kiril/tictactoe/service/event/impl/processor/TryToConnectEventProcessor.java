package com.kiril.tictactoe.service.event.impl.processor;

import com.kiril.tictactoe.dto.event.GameEvent;
import com.kiril.tictactoe.dto.event.GameEventType;
import com.kiril.tictactoe.dto.game.Figure;
import com.kiril.tictactoe.exception.GameConnectionException;
import com.kiril.tictactoe.service.event.EventOrchestrator;
import com.kiril.tictactoe.service.event.EventProcessor;
import com.kiril.tictactoe.service.game.GameRuleService;
import com.kiril.tictactoe.service.net.GameEventSender;
import com.kiril.tictactoe.service.screen.ScreenHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Implementation of {@link EventProcessor}. Handles {@link GameEventType#TRY_TO_CONNECT} events.
 *
 * @author kakshonau
 */
@Component
public class TryToConnectEventProcessor implements EventProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(TryToConnectEventProcessor.class);

    private final GameRuleService gameRuleService;
    private final GameEventSender sender;
    private final ScreenHandler screenHandler;
    private final EventOrchestrator eventOrchestrator;

    public TryToConnectEventProcessor(GameRuleService gameRuleService, GameEventSender sender, ScreenHandler screenHandler,
            EventOrchestrator eventOrchestrator) {
        this.gameRuleService = gameRuleService;
        this.sender = sender;
        this.screenHandler = screenHandler;
        this.eventOrchestrator = eventOrchestrator;
    }

    @Override
    public void processEvent(GameEvent event) {
        LOGGER.info("Connecting to host {}", event.port());

        gameRuleService.tryToInit(Figure.ZERO);
        screenHandler.startDraw();
        try {
            sender.connect(event.port());
        } catch (GameConnectionException e) {
            LOGGER.error("Unexpected exception during connection", e);
            eventOrchestrator.fireEvent(GameEvent.withEvent(GameEventType.STATE_INCONSISTENT));
        }
    }

    @Override
    public GameEventType getEventType() {
        return GameEventType.TRY_TO_CONNECT;
    }

}
