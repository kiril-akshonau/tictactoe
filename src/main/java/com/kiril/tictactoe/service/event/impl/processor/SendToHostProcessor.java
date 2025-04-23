package com.kiril.tictactoe.service.event.impl.processor;

import com.kiril.tictactoe.dto.event.GameEvent;
import com.kiril.tictactoe.dto.event.GameEventType;
import com.kiril.tictactoe.service.event.EventProcessor;
import com.kiril.tictactoe.service.net.GameEventSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Implementation of {@link EventProcessor}. Handles {@link GameEventType#SEND_TO_HOST} events.
 *
 * @author kakshonau
 */
@Component
public class SendToHostProcessor implements EventProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(SendToHostProcessor.class);

    private final GameEventSender sender;

    public SendToHostProcessor(GameEventSender sender) {
        this.sender = sender;
    }

    @Override
    public void processEvent(GameEvent event) {
        LOGGER.debug("Send to host event {}", event);
        sender.sendToHost(GameEvent.withMove(GameEventType.HOST_MOVE, event.gameMove()));
    }

    @Override
    public GameEventType getEventType() {
        return GameEventType.SEND_TO_HOST;
    }

}
