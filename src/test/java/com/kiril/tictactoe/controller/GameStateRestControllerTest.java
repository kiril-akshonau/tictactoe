package com.kiril.tictactoe.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kiril.tictactoe.dto.game.FieldState;
import com.kiril.tictactoe.dto.game.Figure;
import com.kiril.tictactoe.dto.game.GameMove;
import com.kiril.tictactoe.dto.game.Status;
import com.kiril.tictactoe.service.game.GameRuleService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class GameStateRestControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private GameRuleService gameRuleService;
    @Autowired
    private ObjectMapper objectMapper;

    @AfterEach
    void cleanUp() {
        gameRuleService.reInit(new FieldState(new Figure[3][3], Status.OK));
    }

    @Test
    void testGetInitState() throws Exception {
        gameRuleService.tryToInit(Figure.CROSS);
        var expectedJson = objectMapper.writeValueAsString(new Figure[3][3]);

        this.mockMvc.perform(get("/state"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().json(expectedJson));
    }

    @Test
    void testGetStateWithStep() throws Exception {
        gameRuleService.tryToInit(Figure.CROSS);
        gameRuleService.pushMove(new GameMove(Figure.CROSS, 1, 1, 1, null));
        var field = new Figure[3][3];
        field[1][1] = Figure.CROSS;
        var expectedJson = objectMapper.writeValueAsString(field);

        this.mockMvc.perform(get("/state"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().json(expectedJson));
    }
}
