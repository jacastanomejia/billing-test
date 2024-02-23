package com.magnetron.billing.service.contract.base;

import com.magnetron.billing.exception.ApiRestRuntimeException;

import java.util.List;

public interface IQueryByReferenceService<T, U>{
    List<T> getByReference(U id) throws ApiRestRuntimeException;
}
