package com.kiril.tictactoe.service.net.impl;

import com.kiril.tictactoe.exception.GameConnectionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

/**
 * Connecter class.
 *
 * @author kakshonau
 */
@Component
public class GameConnector {

    private static final Logger LOGGER = LoggerFactory.getLogger(GameConnector.class);

    private final GameWebSocketSessionHandler handler;

    public GameConnector(GameWebSocketSessionHandler handler) {
        this.handler = handler;
    }

    /**
     * Connect to game host.
     *
     * @param port port number.
     * @return session instance.
     * @throws GameConnectionException in case of connection exceptions.
     */
    public StompSession connect(int port) throws GameConnectionException {
        try {
            var webSocketClient = new StandardWebSocketClient();
            var stompClient = new WebSocketStompClient(webSocketClient);
            stompClient.setMessageConverter(new MappingJackson2MessageConverter());
            var sessionFuture = stompClient.connectAsync(String.format("ws://localhost:%s/ws", port), handler);
            return sessionFuture.get();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            LOGGER.warn("Interrupted", e);
        } catch (Exception e) {
            throw new GameConnectionException("Can't connect to host", e);
        }
        return null;
    }
}
