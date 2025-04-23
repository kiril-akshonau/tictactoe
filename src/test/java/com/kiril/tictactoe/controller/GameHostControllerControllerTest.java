package com.kiril.tictactoe.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import com.kiril.tictactoe.dto.event.GameEvent;
import com.kiril.tictactoe.dto.event.GameEventType;
import com.kiril.tictactoe.dto.game.FieldState;
import com.kiril.tictactoe.dto.game.Figure;
import com.kiril.tictactoe.dto.game.GameMove;
import com.kiril.tictactoe.dto.game.Status;
import com.kiril.tictactoe.exception.GameConnectionException;
import com.kiril.tictactoe.service.game.FieldOrchestrator;
import com.kiril.tictactoe.service.net.impl.GameEventSenderImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class GameHostControllerControllerTest {

    @Autowired
    private FieldOrchestrator fieldOrchestrator;
    @Autowired
    private GameEventSenderImpl eventSender;
    @Value("${local.server.port}")
    private int port;

    @BeforeEach
    void setUp() throws GameConnectionException {
        eventSender.connect(port);
    }

    @AfterEach
    void cleanUp() {
        fieldOrchestrator.reInit(new FieldState(new Figure[3][3], Status.OK));
        eventSender.tearDown();
    }

    @Test
    @Timeout(value = 5)
    void testConnectionRequest() {
        await().until(() -> Arrays.stream(fieldOrchestrator.getGameField())
                .flatMap(Arrays::stream)
                .filter(Figure.CROSS::equals)
                .count(), is(equalTo(1L)));
    }

    @Test
    @Timeout(value = 5)
    void testFireEvent() {
        await().until(() -> Arrays.stream(fieldOrchestrator.getGameField())
                .flatMap(Arrays::stream)
                .anyMatch(Figure.CROSS::equals));
        var field = fieldOrchestrator.getGameField();
        var emptyMove = null == field[0][0] ? new GameMove(Figure.ZERO, 0, 0, 2, null) : new GameMove(Figure.ZERO, 0, 1, 2, null);

        eventSender.sendToHost(new GameEvent(GameEventType.HOST_MOVE, emptyMove, null, port));
        await().until(() -> Arrays.stream(fieldOrchestrator.getGameField())
                .flatMap(Arrays::stream)
                .filter(Figure.ZERO::equals)
                .count(), is(equalTo(1L)));
        assertThat(Arrays.stream(fieldOrchestrator.getGameField())
                .flatMap(Arrays::stream)
                .filter(Figure.CROSS::equals)
                .count()).isEqualTo(2L);
    }
}
