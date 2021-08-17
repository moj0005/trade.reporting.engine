package com.vanguard.trade.reporting.engine.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vanguard.trade.reporting.engine.service.EventService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;

@WebMvcTest(EventController.class)
class EventControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EventService eventServiceMock;

    @Autowired
    private ObjectMapper objectMapper;

    // TODO many more tests can be written
    // This is the easiest test case I could write.
    // So many other tests should be written but currently lack of time does not permit this and this is just an assignment
    @Test
    void getEventBasedOnCriteria() throws Exception {

        Mockito.when(eventServiceMock.getEventBasedOnCriteria("test.xml")).thenReturn(objectMapper.createArrayNode());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/events").param("fileName", "test.xml")).andExpect(MockMvcResultMatchers.status().isNotFound());

    }



}