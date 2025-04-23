package com.kiril.tictactoe.service.event.impl.processor;

import com.kiril.tictactoe.dto.event.GameEvent;
import com.kiril.tictactoe.dto.event.GameEventType;
import com.kiril.tictactoe.exception.UnknownCommandException;
import com.kiril.tictactoe.service.event.EventProcessor;

/**
 * Implementation of {@link EventProcessor}. Doesn't handle any events, only indicates that event loop tries to process unknown event.
 *
 * @author kakshonau
 */
public class UnknownEventProcessor implements EventProcessor {

    /**
     * Singleton instance of {@link UnknownEventProcessor}.
     */
    public static final EventProcessor INSTANCE = new UnknownEventProcessor();

    private UnknownEventProcessor() {
    }

    @Override
    public void processEvent(GameEvent event) throws InterruptedException {
        throw new UnknownCommandException("Unknown event");
    }

    @Override
    public GameEventType getEventType() {
        return null;
    }
}
