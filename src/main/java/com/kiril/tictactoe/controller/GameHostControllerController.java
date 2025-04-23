package com.kiril.tictactoe.controller;

import com.kiril.tictactoe.dto.event.GameEvent;
import com.kiril.tictactoe.dto.event.GameEventType;
import com.kiril.tictactoe.service.event.EventOrchestrator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

/**
 * Controller for host application to answer for web socket requests.
 *
 * @author kakshonau
 */
@Controller
public class GameHostControllerController {

    private static final Logger LOGGER = LoggerFactory.getLogger(GameHostControllerController.class);

    private final EventOrchestrator eventOrchestrator;

    public GameHostControllerController(EventOrchestrator eventOrchestrator) {
        this.eventOrchestrator = eventOrchestrator;
    }

    /**
     * Fires game events.
     *
     * @param gameEvent game event.
     */
    @MessageMapping("/fireEvent")
    public void fireEvent(GameEvent gameEvent) {
        LOGGER.debug("Received new move [{}]", gameEvent);
        eventOrchestrator.fireEvent(gameEvent);
    }

    /**
     * Indicates that connection was requested from client. Initializes game under hood.
     */
    @MessageMapping("/connectionRequest")
    public void connectionRequest() {
        LOGGER.info("Received new game request");
        eventOrchestrator.fireEvent(GameEvent.withEvent(GameEventType.CONNECTION_RECEIVED));
    }
}
