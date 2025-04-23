package com.kiril.tictactoe.service.screen;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.kiril.tictactoe.dto.game.Figure;
import com.kiril.tictactoe.dto.game.Status;
import com.kiril.tictactoe.service.game.FieldOrchestrator;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.jline.terminal.Terminal;
import org.jline.utils.InfoCmp.Capability;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Class responsible for drawing information on screen.
 *
 * @author kakshonau
 */
@Component
public class ScreenHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScreenHandler.class);
    private static final String STRING_FIELDS_PATTERN = " %s | %s | %s";
    private static final String STRING_LINE_PATTERN = "-----------";

    private ScheduledExecutorService executorService;
    private final Terminal terminal;
    private final FieldOrchestrator gameOrchestrator;

    public ScreenHandler(Terminal terminal, FieldOrchestrator gameOrchestrator) {
        this.terminal = terminal;
        this.gameOrchestrator = gameOrchestrator;
    }

    @PostConstruct
    private void init() {
        executorService = Executors.newScheduledThreadPool(1, new ThreadFactoryBuilder()
                .setNameFormat("screen-handler-%d")
                .setUncaughtExceptionHandler((t, e) -> LOGGER.error("Thread {} throw uncaught exception", t.getName(), e))
                .build());
    }

    @PreDestroy
    private void tearDown() {
        executorService.shutdownNow();
    }

    /**
     * Starts drawing process.
     */
    public void startDraw() {
        executorService.scheduleAtFixedRate(this::redraw, 1, 1, TimeUnit.SECONDS);
    }

    private void redraw() {
        try {
            var gameState = gameOrchestrator.getFieldState();
            var gameField = gameState.getGameField();

            terminal.puts(Capability.clear_screen);
            terminal.writer().println();
            terminal.writer().println(String.format(STRING_FIELDS_PATTERN, getCharacter(gameField[0][0]), getCharacter(gameField[0][1]), getCharacter(gameField[0][2])));
            terminal.writer().println(STRING_LINE_PATTERN);
            terminal.writer().println(String.format(STRING_FIELDS_PATTERN, getCharacter(gameField[1][0]), getCharacter(gameField[1][1]), getCharacter(gameField[1][2])));
            terminal.writer().println(STRING_LINE_PATTERN);
            terminal.writer().println(String.format(STRING_FIELDS_PATTERN, getCharacter(gameField[2][0]), getCharacter(gameField[2][1]), getCharacter(gameField[2][2])));
            terminal.writer().println();

            if (gameState.getStatus() != Status.OK) {
                terminal.writer().println(gameState.getStatus().getCaption());
                terminal.writer().println();
            }
            terminal.flush();
        } catch (Exception e) {
            LOGGER.error("Error during drawing new screen", e);
        }
    }

    private String getCharacter(Figure figure) {
        return null == figure ? " " : figure.getCharacter();
    }
}
