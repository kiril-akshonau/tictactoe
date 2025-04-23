package com.kiril.tictactoe.service.event.impl.processor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

import com.kiril.tictactoe.dto.event.GameEvent;
import com.kiril.tictactoe.dto.event.GameEventType;
import com.kiril.tictactoe.dto.game.FieldState;
import com.kiril.tictactoe.service.event.EventOrchestrator;
import com.kiril.tictactoe.service.game.GameRuleService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ClientReapplyFieldProcessorTest {

    @Mock
    private GameRuleService gameRuleService;
    @Mock
    private EventOrchestrator eventOrchestrator;

    @InjectMocks
    private ClientReapplyFieldProcessor processor;

    @Test
    void testProcessEvent() {
        var fieldState = new FieldState(null, null);
        processor.processEvent(new GameEvent(GameEventType.CLIENT_MOVE, null, fieldState, 0));

        verify(gameRuleService).reInit(fieldState);
        verify(eventOrchestrator).fireEvent(GameEvent.withEvent(GameEventType.CLIENT_MOVE));
    }

    @Test
    void testGetEventType() {
        assertThat(processor.getEventType()).isEqualTo(GameEventType.CLIENT_REAPPLY_FIELD);
    }
}
