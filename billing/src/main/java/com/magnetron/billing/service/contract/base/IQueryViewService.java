package com.magnetron.billing.service.contract.base;

import com.magnetron.billing.exception.ApiRestRuntimeException;

import java.util.List;

public interface IQueryViewService<T> {
    List<T> getAll() throws ApiRestRuntimeException;
}
