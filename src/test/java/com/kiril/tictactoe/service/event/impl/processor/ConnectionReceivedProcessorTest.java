package com.kiril.tictactoe.service.event.impl.processor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.kiril.tictactoe.dto.event.GameEvent;
import com.kiril.tictactoe.dto.event.GameEventType;
import com.kiril.tictactoe.dto.game.FieldState;
import com.kiril.tictactoe.dto.game.Figure;
import com.kiril.tictactoe.dto.game.GameMove;
import com.kiril.tictactoe.dto.game.Status;
import com.kiril.tictactoe.service.event.EventOrchestrator;
import com.kiril.tictactoe.service.game.GameRuleService;
import com.kiril.tictactoe.service.screen.ScreenHandler;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ConnectionReceivedProcessorTest {

    @Mock
    private GameRuleService gameRuleService;
    @Mock
    private ScreenHandler screenHandler;
    @Mock
    private EventOrchestrator eventOrchestrator;

    @InjectMocks
    private ConnectionReceivedProcessor processor;

    @Test
    void testProcessEventReinit() {
        var state = new FieldState(null, null);
        when(gameRuleService.tryToInit(Figure.CROSS)).thenReturn(state);

        processor.processEvent(new GameEvent(GameEventType.CLIENT_MOVE, null, null, 0));

        verify(gameRuleService).tryToInit(Figure.CROSS);
        verify(eventOrchestrator).fireEvent(GameEvent.withFieldState(GameEventType.SEND_TO_CLIENT, state));
    }

    @Test
    void testProcessEventFirstMove() {
        var resultMove = new GameMove(null, 0, 0, 0, Status.OK);
        when(gameRuleService.makeFirstMove()).thenReturn(resultMove);

        processor.processEvent(new GameEvent(GameEventType.CLIENT_MOVE, null, null, 0));

        verify(screenHandler).startDraw();
        verify(eventOrchestrator).fireEvent(GameEvent.withMove(GameEventType.SEND_TO_CLIENT, resultMove));
    }

    @Test
    void testProcessEventWithError() {
        when(gameRuleService.makeFirstMove()).thenReturn(null);

        processor.processEvent(new GameEvent(GameEventType.CLIENT_MOVE, null, null, 0));

        verify(screenHandler).startDraw();
        verify(eventOrchestrator).fireEvent(GameEvent.withEvent(GameEventType.STATE_INCONSISTENT));
    }

    @Test
    void testGetEventType() {
        assertThat(processor.getEventType()).isEqualTo(GameEventType.CONNECTION_RECEIVED);
    }
}
