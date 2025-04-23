package com.kiril.tictactoe.service.event;

import com.kiril.tictactoe.dto.event.GameEvent;

/**
 * Service for take and submit events.
 *
 * @author kakshonau
 */
public interface EventOrchestrator {

    /**
     * Sends event to processing.
     *
     * @param event new event to process.
     */
    void fireEvent(GameEvent event);

    /**
     * Takes next event. Blocking operation.
     *
     * @return next event.
     * @throws InterruptedException if thread was interrupted during waiting.
     */
    GameEvent take() throws InterruptedException;
}