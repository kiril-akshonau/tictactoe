package com.kiril.tictactoe.service.event.impl.processor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.kiril.tictactoe.dto.event.GameEvent;
import com.kiril.tictactoe.dto.event.GameEventType;
import com.kiril.tictactoe.dto.game.GameMove;
import com.kiril.tictactoe.dto.game.Status;
import com.kiril.tictactoe.service.event.EventOrchestrator;
import com.kiril.tictactoe.service.game.GameRuleService;
import com.kiril.tictactoe.service.net.GameEventSender;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class HostMoveProcessorTest {

    @Mock
    private GameRuleService gameRuleService;
    @Mock
    private EventOrchestrator eventOrchestrator;
    @Mock
    private GameEventSender sender;

    @InjectMocks
    private HostMoveProcessor processor;

    @Test
    void testProcessEventGoodResult() throws InterruptedException {
        var resultMove = new GameMove(null, 0, 0, 0, Status.OK);
        when(gameRuleService.makeMove(resultMove)).thenReturn(resultMove);

        processor.processEvent(new GameEvent(GameEventType.CLIENT_MOVE, resultMove, null, 0));

        verify(gameRuleService).makeMove(resultMove);
        verify(eventOrchestrator).fireEvent(GameEvent.withMove(GameEventType.SEND_TO_CLIENT, resultMove));
    }

    @Test
    void testProcessEventFinishedResult() throws InterruptedException {
        var resultMove = new GameMove(null, 0, 0, 0, Status.FINISHED);
        when(gameRuleService.makeMove(resultMove)).thenReturn(resultMove);

        processor.processEvent(new GameEvent(GameEventType.CLIENT_MOVE, resultMove, null, 0));

        verify(gameRuleService).makeMove(resultMove);
        verify(sender).sendToClient(GameEvent.withMove(GameEventType.GAME_FINISHED, resultMove));
        verify(eventOrchestrator).fireEvent(GameEvent.withEvent(GameEventType.GAME_FINISHED));
    }

    @Test
    void testProcessEventErrorResult() throws InterruptedException {
        var resultMove = new GameMove(null, 0, 0, 0, Status.ERROR);
        when(gameRuleService.makeMove(resultMove)).thenReturn(resultMove);

        processor.processEvent(new GameEvent(GameEventType.CLIENT_MOVE, resultMove, null, 0));

        verify(gameRuleService).makeMove(resultMove);
        verify(eventOrchestrator).fireEvent(GameEvent.withEvent(GameEventType.STATE_INCONSISTENT));
    }

    @Test
    void testGetEventType() {
        assertThat(processor.getEventType()).isEqualTo(GameEventType.HOST_MOVE);
    }
}
