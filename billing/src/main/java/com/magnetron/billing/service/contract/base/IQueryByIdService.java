package com.magnetron.billing.service.contract.base;

import com.magnetron.billing.exception.ApiRestRuntimeException;

public interface IQueryByIdService<T, U>{
    T getById(U id) throws ApiRestRuntimeException;
}
