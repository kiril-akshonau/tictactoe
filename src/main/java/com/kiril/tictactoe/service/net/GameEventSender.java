package com.kiril.tictactoe.service.net;

import com.kiril.tictactoe.dto.event.GameEvent;
import com.kiril.tictactoe.exception.GameConnectionException;
import org.springframework.messaging.simp.stomp.StompSessionHandler;

/**
 * Class contains various methods that sends information between client and host. 
 *
 * @author kakshonau
 */
public interface GameEventSender {

    /**
     * Connects to game host.
     *
     * @param port port number.
     * @throws GameConnectionException connection exception.
     */
    void connect(int port) throws GameConnectionException;

    /**
     * Subscribe to web socket.
     *
     * @param handler instance of {@link StompSessionHandler}.
     */
    void subscribe(StompSessionHandler handler);

    /**
     * Sends event from client to host.
     *
     * @param event event to send.
     */
    void sendToHost(GameEvent event);

    /**
     * Sends event from host to client.
     *
     * @param event event to send.
     */
    void sendToClient(GameEvent event);

}