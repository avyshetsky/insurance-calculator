package com.scale.global.insurance.app.service;

import com.scale.global.insurance.app.api.CustomerRequest;
import com.scale.global.insurance.app.api.CustomerResponse;

import java.util.List;

public interface CustomerService extends com.scale.global.insurance.app.api.CustomerService {

    @Override
    CustomerResponse findById(Integer id);

    @Override
    List<CustomerResponse> findAll();

    @Override
    CustomerResponse create(CustomerRequest request);

    @Override
    CustomerResponse update(Integer id, CustomerRequest request);

    @Override
    void deleteById(Integer id);
}
