package com.kiril.tictactoe.service.event.impl;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.kiril.tictactoe.dto.event.GameEvent;
import com.kiril.tictactoe.dto.event.GameEventType;
import com.kiril.tictactoe.service.event.EventOrchestrator;
import com.kiril.tictactoe.service.event.EventProcessor;
import com.kiril.tictactoe.service.event.impl.processor.UnknownEventProcessor;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Runs events from queue.
 *
 * @author kakshonau
 */
@Component
public class EventLoop {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventLoop.class);

    private final EventOrchestrator eventOrchestrator;
    private Map<GameEventType, EventProcessor> processorRegistry;
    private ExecutorService executorService;

    public EventLoop(EventOrchestrator eventOrchestrator, List<EventProcessor> processors) {
        this.eventOrchestrator = eventOrchestrator;
        this.processorRegistry = processors.stream()
                .collect(Collectors.toMap(EventProcessor::getEventType, Function.identity()));
    }

    @PostConstruct
    private void init() {
        executorService = Executors.newSingleThreadExecutor(new ThreadFactoryBuilder()
                .setNameFormat("event-loop-%d")
                .setUncaughtExceptionHandler((t, e) -> LOGGER.error("Thread {} throw uncaught exception", t.getName(), e))
                .build());
        executorService.submit(this::eventLoop);
    }

    @PreDestroy
    private void tearDown() {
        executorService.shutdownNow();
    }

    private void eventLoop() {
        while (!Thread.currentThread().isInterrupted()) {
            GameEvent event = null;
            try {
                event = eventOrchestrator.take();
                processEvent(event);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                LOGGER.debug("Interrupted", e);
            } catch (Exception e) {
                LOGGER.error("Unexpected error during processing event {}", event, e);
            }
        }
    }

    private void processEvent(GameEvent event) throws InterruptedException {
        processorRegistry.getOrDefault(event.eventType(), UnknownEventProcessor.INSTANCE).processEvent(event);
    }
}
