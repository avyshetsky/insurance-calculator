package com.scale.global.insurance.app.repositories;

import com.scale.global.insurance.app.entity.Customer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ContextConfiguration(classes = CustomerRepositoryTest.TestConfig.class)
class CustomerRepositoryTest {

    @EnableAutoConfiguration
    @EntityScan(basePackageClasses = Customer.class)
    @EnableJpaRepositories(basePackageClasses = CustomerRepository.class)
    static class TestConfig {
    }

    @Autowired
    CustomerRepository customerRepository;

    @Test
    void saveAndFindById() {
        Customer customer = Customer.builder()
                .firstName("John")
                .lastName("Doe")
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .inceptionOfThePolicy(LocalDate.of(2020, 6, 1))
                .build();

        Customer saved = customerRepository.save(customer);
        Optional<Customer> found = customerRepository.findById(saved.getInsuranceNumber());

        assertTrue(found.isPresent());
        assertEquals("John", found.get().getFirstName());
        assertEquals("Doe", found.get().getLastName());
    }

    @Test
    void findAll() {
        customerRepository.save(Customer.builder()
                .firstName("A").lastName("B")
                .dateOfBirth(LocalDate.of(1985, 3, 15))
                .inceptionOfThePolicy(LocalDate.of(2018, 1, 1))
                .build());
        customerRepository.save(Customer.builder()
                .firstName("C").lastName("D")
                .dateOfBirth(LocalDate.of(1992, 7, 20))
                .inceptionOfThePolicy(LocalDate.of(2019, 5, 10))
                .build());

        long count = customerRepository.count();
        assertEquals(2, count);
    }

    @Test
    void deleteById() {
        Customer saved = customerRepository.save(Customer.builder()
                .firstName("Delete").lastName("Me")
                .dateOfBirth(LocalDate.of(2000, 1, 1))
                .inceptionOfThePolicy(LocalDate.of(2022, 1, 1))
                .build());

        customerRepository.deleteById(saved.getInsuranceNumber());
        assertFalse(customerRepository.findById(saved.getInsuranceNumber()).isPresent());
    }
}
