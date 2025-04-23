package com.kiril.tictactoe.service.event.impl.processor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

import com.kiril.tictactoe.dto.event.GameEvent;
import com.kiril.tictactoe.dto.event.GameEventType;
import com.kiril.tictactoe.dto.game.GameMove;
import com.kiril.tictactoe.service.net.GameEventSender;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SendToHostProcessorTest {

    @Mock
    private GameEventSender sender;

    @InjectMocks
    private SendToHostProcessor processor;

    @Test
    void testProcessEvent() {
        var gameMove = new GameMove(null, 0, 0, 0, null);
        var event = GameEvent.withMove(GameEventType.SEND_TO_HOST, gameMove);

        processor.processEvent(event);

        verify(sender).sendToHost(GameEvent.withMove(GameEventType.HOST_MOVE, event.gameMove()));
    }

    @Test
    void testGetEventType() {
        assertThat(processor.getEventType()).isEqualTo(GameEventType.SEND_TO_HOST);
    }
}
