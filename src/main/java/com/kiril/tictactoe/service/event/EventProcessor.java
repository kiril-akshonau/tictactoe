package com.kiril.tictactoe.service.event;

import com.kiril.tictactoe.dto.event.GameEvent;
import com.kiril.tictactoe.dto.event.GameEventType;

/**
 * Interface for event processors. Capable to process various {@link GameEvent} events.
 *
 * @author kakshonau
 */
public interface EventProcessor {

    /**
     * Process incoming event.
     *
     * @param event incoming {@link GameEvent} event.
     * @throws InterruptedException only because of {@code Thread.sleep} in some implementations. Need to simulate delay.
     */
    void processEvent(GameEvent event) throws InterruptedException;

    /**
     * Gets supported event type.
     *
     * @return supported {@link GameEventType}.
     */
    GameEventType getEventType();
}
