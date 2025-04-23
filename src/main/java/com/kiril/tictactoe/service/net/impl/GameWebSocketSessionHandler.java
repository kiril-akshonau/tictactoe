package com.kiril.tictactoe.service.net.impl;

import com.kiril.tictactoe.dto.event.GameEvent;
import com.kiril.tictactoe.dto.event.GameEventType;
import com.kiril.tictactoe.service.event.EventOrchestrator;
import com.kiril.tictactoe.service.net.GameEventSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;

/**
 * Custom implementation of {@link StompSessionHandlerAdapter}.
 *
 * @author kakshonau
 */
@Component
public class GameWebSocketSessionHandler extends StompSessionHandlerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(GameWebSocketSessionHandler.class);

    private final EventOrchestrator eventOrchestrator;
    private final GameEventSender eventSender;

    public GameWebSocketSessionHandler(EventOrchestrator eventOrchestrator, @Lazy GameEventSender eventSender) {
        this.eventOrchestrator = eventOrchestrator;
        this.eventSender = eventSender;
    }

    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
        eventSender.subscribe(this);
        LOGGER.debug("New session: {}", session.getSessionId());
    }

    @Override
    public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {
        LOGGER.error("Unexpected exception", exception);
        eventOrchestrator.fireEvent(GameEvent.withEvent(GameEventType.STATE_INCONSISTENT));
    }

    @Override
    public void handleTransportError(StompSession session, Throwable exception) {
        LOGGER.error("Transport error", exception);
        eventOrchestrator.fireEvent(GameEvent.withEvent(GameEventType.STATE_INCONSISTENT));
    }

    @Override
    public Type getPayloadType(StompHeaders headers) {
        return GameEvent.class;
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
        LOGGER.debug("Received: {}", payload);
        eventOrchestrator.fireEvent((GameEvent) payload);
    }
}
