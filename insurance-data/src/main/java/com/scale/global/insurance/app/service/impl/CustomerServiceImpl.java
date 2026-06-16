package com.scale.global.insurance.app.service.impl;

import com.scale.global.insurance.app.api.CustomerNotFoundException;
import com.scale.global.insurance.app.api.CustomerRequest;
import com.scale.global.insurance.app.api.CustomerResponse;
import com.scale.global.insurance.app.engine.PriceCalculator;
import com.scale.global.insurance.app.entity.Customer;
import com.scale.global.insurance.app.repositories.CustomerRepository;
import com.scale.global.insurance.app.service.CustomerService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final PriceCalculator priceCalculator;

    public CustomerServiceImpl(CustomerRepository customerRepository, PriceCalculator priceCalculator) {
        this.customerRepository = customerRepository;
        this.priceCalculator = priceCalculator;
    }

    @Override
    public CustomerResponse findById(Integer id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(id));
        return toResponse(customer);
    }

    @Override
    public List<CustomerResponse> findAll() {
        return StreamSupport.stream(customerRepository.findAll().spliterator(), false)
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public CustomerResponse create(CustomerRequest request) {
        Customer customer = toEntity(request);
        Customer saved = customerRepository.save(customer);
        return toResponse(saved);
    }

    @Override
    public CustomerResponse update(Integer id, CustomerRequest request) {
        Customer customer = toEntity(request);
        customer.setInsuranceNumber(id);
        Customer saved = customerRepository.save(customer);
        return toResponse(saved);
    }

    @Override
    public void deleteById(Integer id) {
        customerRepository.deleteById(id);
    }

    private CustomerResponse toResponse(Customer customer) {
        BigDecimal rate = priceCalculator.calculateRate(
                customer.getDateOfBirth(), customer.getInceptionOfThePolicy());
        return new CustomerResponse(
                customer.getInsuranceNumber(),
                customer.getFirstName(),
                customer.getLastName(),
                customer.getDateOfBirth(),
                customer.getInceptionOfThePolicy(),
                rate
        );
    }

    private Customer toEntity(CustomerRequest request) {
        return Customer.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .dateOfBirth(request.dateOfBirth())
                .inceptionOfThePolicy(request.inceptionOfThePolicy())
                .build();
    }
}
