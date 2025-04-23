package com.kiril.tictactoe.service.event.impl.processor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

import com.kiril.tictactoe.dto.event.GameEvent;
import com.kiril.tictactoe.dto.event.GameEventType;
import com.kiril.tictactoe.dto.game.GameMove;
import com.kiril.tictactoe.service.game.GameRuleService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GameInconsistentProcessorTest {

    @Mock
    private GameRuleService gameRuleService;

    @InjectMocks
    private GameInconsistentProcessor processor;

    @Test
    void testProcessEvent() throws InterruptedException {
        var gameMove = new GameMove(null, 0, 0, 0, null);
        var event = GameEvent.withMove(GameEventType.GAME_FINISHED, gameMove);

        processor.processEvent(event);

        verify(gameRuleService).markGameAsInconsistent();
    }

    @Test
    void testGetEventType() {
        assertThat(processor.getEventType()).isEqualTo(GameEventType.STATE_INCONSISTENT);
    }
}
