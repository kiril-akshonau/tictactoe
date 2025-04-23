package com.kiril.tictactoe.service.event.impl.processor;

import com.kiril.tictactoe.dto.event.GameEvent;
import com.kiril.tictactoe.dto.event.GameEventType;
import com.kiril.tictactoe.service.event.EventOrchestrator;
import com.kiril.tictactoe.service.event.EventProcessor;
import com.kiril.tictactoe.service.game.GameRuleService;
import com.kiril.tictactoe.service.net.GameEventSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Implementation of {@link EventProcessor}. Handles {@link GameEventType#HOST_MOVE} events.
 *
 * @author kakshonau
 */
@Component
public class HostMoveProcessor implements EventProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(HostMoveProcessor.class);

    private final GameRuleService gameRuleService;
    private final EventOrchestrator eventOrchestrator;
    private final GameEventSender sender;

    public HostMoveProcessor(GameRuleService gameRuleService, EventOrchestrator eventOrchestrator, GameEventSender sender) {
        this.gameRuleService = gameRuleService;
        this.eventOrchestrator = eventOrchestrator;
        this.sender = sender;
    }

    @Override
    public void processEvent(GameEvent event) throws InterruptedException {
        LOGGER.debug("Received move from client {}", event);

        // simulating delay
        Thread.sleep(1_000);
        var gameMove = gameRuleService.makeMove(event.gameMove());

        switch (gameMove.status()) {
            case OK: {
                eventOrchestrator.fireEvent(GameEvent.withMove(GameEventType.SEND_TO_CLIENT, gameMove));
                break;
            }
            case ERROR: {
                LOGGER.error("Unexpected exception. Can't make move from client [{}]", event);
                eventOrchestrator.fireEvent(GameEvent.withEvent(GameEventType.STATE_INCONSISTENT));
                break;
            }
            case FINISHED: {
                LOGGER.info("Game is finished");
                sender.sendToClient(GameEvent.withMove(GameEventType.GAME_FINISHED, gameMove));
                eventOrchestrator.fireEvent(GameEvent.withEvent(GameEventType.GAME_FINISHED));
                break;
            }
        }
    }

    @Override
    public GameEventType getEventType() {
        return GameEventType.HOST_MOVE;
    }

}
