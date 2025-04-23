package com.kiril.tictactoe.service.event.impl.processor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.kiril.tictactoe.exception.UnknownCommandException;
import com.kiril.tictactoe.service.event.EventProcessor;
import org.junit.jupiter.api.Test;

class UnknownEventProcessorTest {

    private EventProcessor processor = UnknownEventProcessor.INSTANCE;

    @Test
    void testProcessEvent() {
        assertThatThrownBy(() -> processor.processEvent(null)).isInstanceOf(UnknownCommandException.class);
    }

    @Test
    void testGetEventType() {
        assertThat(processor.getEventType()).isNull();
    }
}
