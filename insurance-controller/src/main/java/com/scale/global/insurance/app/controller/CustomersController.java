package com.scale.global.insurance.app.controller;

import com.scale.global.insurance.app.api.CustomerRequest;
import com.scale.global.insurance.app.api.CustomerResponse;
import com.scale.global.insurance.app.api.CustomerService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customers")
@Validated
public class CustomersController {

    private final CustomerService customerService;

    public CustomersController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public List<CustomerResponse> getCustomers() {
        return customerService.findAll();
    }

    @GetMapping("/{id}")
    public CustomerResponse getCustomer(@PathVariable @Min(1) Integer id) {
        return customerService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CustomerResponse saveCustomer(@RequestBody @Valid CustomerRequest request) {
        return customerService.create(request);
    }

    @PutMapping("/{id}")
    public CustomerResponse updateCustomer(@PathVariable @Min(1) Integer id,
                                           @RequestBody @Valid CustomerRequest request) {
        return customerService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCustomer(@PathVariable Integer id) {
        customerService.deleteById(id);
    }
}
