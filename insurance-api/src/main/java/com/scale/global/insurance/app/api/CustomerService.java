package com.scale.global.insurance.app.api;

import java.util.List;

public interface CustomerService {

    CustomerResponse findById(Integer id);

    List<CustomerResponse> findAll();

    CustomerResponse create(CustomerRequest request);

    CustomerResponse update(Integer id, CustomerRequest request);

    void deleteById(Integer id);
}
