package com.kiril.tictactoe;

import com.kiril.tictactoe.dto.event.GameEvent;
import com.kiril.tictactoe.dto.event.GameEventType;
import com.kiril.tictactoe.service.event.EventOrchestrator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Scanner;

/**
 * Application enter point.
 *
 * @author kakshonau
 */
@SpringBootApplication
public class TictactoeApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(TictactoeApplication.class);

    public static void main(String[] args) {
        var context = SpringApplication.run(TictactoeApplication.class, args);
        var eventOrchestrator = context.getBean(EventOrchestrator.class);

        LOGGER.info("To start a game please enter port number:");
        Scanner scanner = new Scanner(System.in);
        var port = scanner.nextInt();

        eventOrchestrator.fireEvent(GameEvent.withPort(GameEventType.TRY_TO_CONNECT, port));
    }

}
