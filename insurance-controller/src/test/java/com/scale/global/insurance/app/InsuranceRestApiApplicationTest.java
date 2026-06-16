package com.scale.global.insurance.app;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scale.global.insurance.app.api.CustomerRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class InsuranceRestApiApplicationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void contextLoads() {
    }

    @Test
    void fullCrudLifecycle() throws Exception {
        CustomerRequest request = new CustomerRequest(
                "Integration",
                "Tester",
                LocalDate.of(1990, 5, 15),
                LocalDate.of(2020, 1, 1)
        );

        // Create
        String responseJson = mockMvc.perform(post("/api/v1/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.insuranceNumber", notNullValue()))
                .andExpect(jsonPath("$.firstName", equalTo("Integration")))
                .andExpect(jsonPath("$.rate", notNullValue()))
                .andReturn().getResponse().getContentAsString();

        Integer id = objectMapper.readTree(responseJson).get("insuranceNumber").asInt();

        // Read
        mockMvc.perform(get("/api/v1/customers/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", equalTo("Integration")));

        // Update
        CustomerRequest updateRequest = new CustomerRequest(
                "Updated",
                "Tester",
                LocalDate.of(1990, 5, 15),
                LocalDate.of(2020, 1, 1)
        );
        mockMvc.perform(put("/api/v1/customers/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", equalTo("Updated")));

        // List
        mockMvc.perform(get("/api/v1/customers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));

        // Delete
        mockMvc.perform(delete("/api/v1/customers/" + id))
                .andExpect(status().isNoContent());

        // Verify deleted
        mockMvc.perform(get("/api/v1/customers/" + id))
                .andExpect(status().isNotFound());
    }

    @Test
    void validationErrors() throws Exception {
        CustomerRequest invalidRequest = new CustomerRequest(
                "A",
                "B",
                LocalDate.now().plusYears(1),
                LocalDate.now().plusYears(1)
        );

        mockMvc.perform(post("/api/v1/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }
}
