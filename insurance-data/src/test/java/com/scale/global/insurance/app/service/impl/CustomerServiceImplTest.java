package com.scale.global.insurance.app.service.impl;

import com.scale.global.insurance.app.api.CustomerNotFoundException;
import com.scale.global.insurance.app.api.CustomerRequest;
import com.scale.global.insurance.app.api.CustomerResponse;
import com.scale.global.insurance.app.engine.PriceCalculator;
import com.scale.global.insurance.app.entity.Customer;
import com.scale.global.insurance.app.repositories.CustomerRepository;
import com.scale.global.insurance.app.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CustomerServiceImplTest {

    @Mock
    CustomerRepository customerRepository;
    @Mock
    PriceCalculator priceCalculator;

    CustomerService customerService;

    Customer customer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        customerService = new CustomerServiceImpl(customerRepository, priceCalculator);
        customer = Customer.builder()
                .insuranceNumber(123)
                .firstName("Tirion")
                .lastName("Lannister")
                .dateOfBirth(LocalDate.now().minusYears(30))
                .inceptionOfThePolicy(LocalDate.now().minusYears(5).minusDays(5))
                .build();
    }

    @Test
    void findById() {
        when(customerRepository.findById(anyInt())).thenReturn(Optional.of(customer));
        when(priceCalculator.calculateRate(any(), any())).thenReturn(new BigDecimal("10.23"));
        CustomerResponse response = customerService.findById(1);

        assertEquals(customer.getInsuranceNumber(), response.insuranceNumber());
        assertEquals(customer.getFirstName(), response.firstName());
        assertEquals(customer.getLastName(), response.lastName());
        assertEquals(customer.getDateOfBirth(), response.dateOfBirth());
        assertEquals(customer.getInceptionOfThePolicy(), response.inceptionOfThePolicy());
        assertEquals(new BigDecimal("10.23"), response.rate());
    }

    @Test
    void findByIdException() {
        when(customerRepository.findById(anyInt())).thenReturn(Optional.empty());
        assertThrows(CustomerNotFoundException.class, () -> customerService.findById(31));
    }

    @Test
    void findAll() {
        when(customerRepository.findAll()).thenReturn(Arrays.asList(customer, customer));
        when(priceCalculator.calculateRate(any(), any())).thenReturn(BigDecimal.ZERO);
        List<CustomerResponse> all = customerService.findAll();
        assertEquals(2, all.size());
        verify(customerRepository, times(1)).findAll();
    }

    @Test
    void create() {
        when(customerRepository.save(any())).thenReturn(customer);
        when(priceCalculator.calculateRate(any(), any())).thenReturn(new BigDecimal("10.23"));
        CustomerRequest request = new CustomerRequest(
                "Tirion", "Lannister",
                LocalDate.now().minusYears(30),
                LocalDate.now().minusYears(5).minusDays(5)
        );
        CustomerResponse saved = customerService.create(request);
        assertNotNull(saved);
        assertEquals(customer.getInsuranceNumber(), saved.insuranceNumber());
        assertEquals(customer.getFirstName(), saved.firstName());
    }

    @Test
    void update() {
        when(customerRepository.save(any())).thenReturn(customer);
        when(priceCalculator.calculateRate(any(), any())).thenReturn(new BigDecimal("10.23"));
        CustomerRequest request = new CustomerRequest(
                "Tirion", "Lannister",
                LocalDate.now().minusYears(30),
                LocalDate.now().minusYears(5).minusDays(5)
        );
        CustomerResponse updated = customerService.update(123, request);
        assertNotNull(updated);
        assertEquals(customer.getInsuranceNumber(), updated.insuranceNumber());
    }

    @Test
    void deleteById() {
        customerService.deleteById(1);
        verify(customerRepository, times(1)).deleteById(anyInt());
    }
}
