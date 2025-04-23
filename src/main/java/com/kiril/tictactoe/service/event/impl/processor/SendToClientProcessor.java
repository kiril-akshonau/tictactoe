package com.kiril.tictactoe.service.event.impl.processor;

import com.kiril.tictactoe.dto.event.GameEvent;
import com.kiril.tictactoe.dto.event.GameEventType;
import com.kiril.tictactoe.service.event.EventProcessor;
import com.kiril.tictactoe.service.net.GameEventSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Implementation of {@link EventProcessor}. Handles {@link GameEventType#SEND_TO_CLIENT} events.
 *
 * @author kakshonau
 */
@Component
public class SendToClientProcessor implements EventProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(SendToClientProcessor.class);

    private final GameEventSender sender;

    public SendToClientProcessor(GameEventSender sender) {
        this.sender = sender;
    }

    @Override
    public void processEvent(GameEvent event) {
        LOGGER.debug("Send to client event {}", event);
        if (null == event.fieldState()) {
            sender.sendToClient(GameEvent.withMove(GameEventType.CLIENT_MOVE, event.gameMove()));
        } else {
            sender.sendToClient(GameEvent.withFieldState(GameEventType.CLIENT_REAPPLY_FIELD, event.fieldState()));
        }
    }

    @Override
    public GameEventType getEventType() {
        return GameEventType.SEND_TO_CLIENT;
    }

}
