package com.scale.global.insurance.app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.scale.global.insurance.app.api.CustomerNotFoundException;
import com.scale.global.insurance.app.api.CustomerRequest;
import com.scale.global.insurance.app.api.CustomerResponse;
import com.scale.global.insurance.app.api.CustomerService;
import com.scale.global.insurance.app.exceptionHandling.ControllerExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CustomersControllerTest {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    @Mock
    CustomerService customerService;

    CustomersController customersController;

    MockMvc mockMvc;

    CustomerResponse customerResponse;
    CustomerRequest customerRequest;
    ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        customersController = new CustomersController(customerService);
        mockMvc = MockMvcBuilders.standaloneSetup(customersController)
                .setControllerAdvice(new ControllerExceptionHandler())
                .build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        customerResponse = new CustomerResponse(
                123,
                "Tirion",
                "Lannister",
                LocalDate.now().minusYears(30),
                LocalDate.now().minusYears(5).minusDays(5),
                new BigDecimal("10.23")
        );

        customerRequest = new CustomerRequest(
                "Tirion",
                "Lannister",
                LocalDate.now().minusYears(30),
                LocalDate.now().minusYears(5).minusDays(5)
        );
    }

    @Test
    void getCustomers() throws Exception {
        CustomerResponse empty = new CustomerResponse(null, null, null, null, null, null);
        when(customerService.findAll()).thenReturn(List.of(empty, empty));

        mockMvc.perform(get("/api/v1/customers"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)));

        verify(customerService, times(1)).findAll();
    }

    @Test
    void getCustomer() throws Exception {
        when(customerService.findById(anyInt())).thenReturn(customerResponse);

        mockMvc.perform(get("/api/v1/customers/12"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.insuranceNumber", equalTo(customerResponse.insuranceNumber())))
                .andExpect(jsonPath("$.firstName", equalTo(customerResponse.firstName())))
                .andExpect(jsonPath("$.lastName", equalTo(customerResponse.lastName())))
                .andExpect(jsonPath("$.dateOfBirth", equalTo(customerResponse.dateOfBirth().format(DATE_FORMATTER))))
                .andExpect(jsonPath("$.inceptionOfThePolicy", equalTo(customerResponse.inceptionOfThePolicy().format(DATE_FORMATTER))))
                .andExpect(jsonPath("$.rate").value(is(customerResponse.rate()), BigDecimal.class));

        verify(customerService, times(1)).findById(anyInt());
    }

    @Test
    void saveCustomer() throws Exception {
        when(customerService.create(any())).thenReturn(customerResponse);

        mockMvc.perform(post("/api/v1/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customerRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.insuranceNumber", equalTo(customerResponse.insuranceNumber())))
                .andExpect(jsonPath("$.firstName", equalTo(customerResponse.firstName())))
                .andExpect(jsonPath("$.lastName", equalTo(customerResponse.lastName())));

        verify(customerService, times(1)).create(any());
    }

    @Test
    void updateCustomer() throws Exception {
        when(customerService.update(eq(123), any())).thenReturn(customerResponse);

        mockMvc.perform(put("/api/v1/customers/123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customerRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.insuranceNumber", equalTo(customerResponse.insuranceNumber())))
                .andExpect(jsonPath("$.firstName", equalTo(customerResponse.firstName())))
                .andExpect(jsonPath("$.lastName", equalTo(customerResponse.lastName())));

        verify(customerService, times(1)).update(eq(123), any());
    }

    @Test
    void deleteCustomer() throws Exception {
        mockMvc.perform(delete("/api/v1/customers/123"))
                .andExpect(status().isNoContent());

        verify(customerService, times(1)).deleteById(anyInt());
    }

    @Test
    void getCustomerNotFound() throws Exception {
        when(customerService.findById(anyInt())).thenThrow(new CustomerNotFoundException(99));

        mockMvc.perform(get("/api/v1/customers/99"))
                .andExpect(status().isNotFound());
    }
}
