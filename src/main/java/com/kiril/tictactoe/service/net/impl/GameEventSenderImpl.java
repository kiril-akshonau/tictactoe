package com.kiril.tictactoe.service.net.impl;

import com.kiril.tictactoe.dto.event.GameEvent;
import com.kiril.tictactoe.exception.GameConnectionException;
import com.kiril.tictactoe.service.net.GameEventSender;
import jakarta.annotation.PreDestroy;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.stereotype.Component;

/**
 * Implementation of {@link GameEventSender}.
 *
 * @author kakshonau
 */
@Component
public class GameEventSenderImpl implements GameEventSender {

    private final GameConnector connector;
    private final SimpMessagingTemplate messagingTemplate;
    private StompSession session;

    public GameEventSenderImpl(GameConnector connector, SimpMessagingTemplate messagingTemplate) {
        this.connector = connector;
        this.messagingTemplate = messagingTemplate;
    }

    /**
     * Closes session.
     */
    @PreDestroy
    public synchronized void tearDown() {
        if (null != session) {
            session.disconnect();
        }
    }

    // session object is not thread safe
    @Override
    public synchronized void connect(int port) throws GameConnectionException {
        session = connector.connect(port);
        session.send("/app/connectionRequest", null);
    }

    @Override
    public synchronized void subscribe(StompSessionHandler handler) {
        session.subscribe("/topic/tictactoe_sub", handler);
    }

    @Override
    public synchronized void sendToHost(GameEvent event) {
        session.send("/app/fireEvent", event);
    }

    @Override
    public void sendToClient(GameEvent event) {
        messagingTemplate.convertAndSend("/topic/tictactoe_sub", event);
    }
}
