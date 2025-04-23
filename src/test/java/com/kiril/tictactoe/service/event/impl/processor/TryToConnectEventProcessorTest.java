package com.kiril.tictactoe.service.event.impl.processor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.kiril.tictactoe.dto.event.GameEvent;
import com.kiril.tictactoe.dto.event.GameEventType;
import com.kiril.tictactoe.dto.game.Figure;
import com.kiril.tictactoe.exception.GameConnectionException;
import com.kiril.tictactoe.service.event.EventOrchestrator;
import com.kiril.tictactoe.service.game.GameRuleService;
import com.kiril.tictactoe.service.net.GameEventSender;
import com.kiril.tictactoe.service.screen.ScreenHandler;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TryToConnectEventProcessorTest {

    @Mock
    private GameRuleService gameRuleService;
    @Mock
    private GameEventSender sender;
    @Mock
    private ScreenHandler screenHandler;
    @Mock
    private EventOrchestrator eventOrchestrator;

    @InjectMocks
    private TryToConnectEventProcessor processor;

    @Test
    void testProcessEvent() throws GameConnectionException {
        when(gameRuleService.tryToInit(Figure.ZERO)).thenReturn(null);

        var event = GameEvent.withPort(GameEventType.TRY_TO_CONNECT, 0);

        processor.processEvent(event);

        verify(gameRuleService).tryToInit(Figure.ZERO);
        verify(screenHandler).startDraw();
        verify(sender).connect(0);
    }

    @Test
    void testProcessEventWithError() throws GameConnectionException {
        when(gameRuleService.tryToInit(Figure.ZERO)).thenReturn(null);
        doThrow(new GameConnectionException("test exception", null)).when(sender).connect(0);

        var event = GameEvent.withPort(GameEventType.TRY_TO_CONNECT, 0);

        processor.processEvent(event);

        verify(gameRuleService).tryToInit(Figure.ZERO);
        verify(screenHandler).startDraw();
        verify(sender).connect(0);
        verify(eventOrchestrator).fireEvent(GameEvent.withEvent(GameEventType.STATE_INCONSISTENT));
    }

    @Test
    void testGetEventType() {
        assertThat(processor.getEventType()).isEqualTo(GameEventType.TRY_TO_CONNECT);
    }
}
