package com.kiril.tictactoe.service.event.impl;

import com.kiril.tictactoe.dto.event.GameEvent;
import com.kiril.tictactoe.service.event.EventOrchestrator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Implementation of {@link EventOrchestrator}.
 *
 * @author kakshonau
 */
@Component
public class EventOrchestratorImpl implements EventOrchestrator {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventOrchestratorImpl.class);

    private final BlockingQueue<GameEvent> eventQueue = new ArrayBlockingQueue<>(8);

    @Override
    public void fireEvent(GameEvent event) {
        try {
            eventQueue.put(event);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            LOGGER.warn("Interrupted", e);
        }
    }

    @Override
    public GameEvent take() throws InterruptedException {
        return eventQueue.take();
    }
}
