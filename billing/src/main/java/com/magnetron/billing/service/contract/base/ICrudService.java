package com.magnetron.billing.service.contract.base;


import com.magnetron.billing.exception.ApiRestRuntimeException;

import java.util.List;


public interface ICrudService<T, U> {
	
	List<T> getAll() throws ApiRestRuntimeException;
	
	T create(T data) throws ApiRestRuntimeException;

	T update(U id, T data) throws ApiRestRuntimeException;
	
	boolean delete(U id) throws ApiRestRuntimeException;
	
}
