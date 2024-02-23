package com.magnetron.billing.service.exception;

import com.magnetron.billing.enumeration.InnerError;
import com.magnetron.billing.exception.ApiRestRuntimeException;

import java.io.Serial;

public class RecordNotFoundException extends ApiRestRuntimeException {

	@Serial
	private static final long serialVersionUID = -8918136139277701834L;

	public RecordNotFoundException(InnerError innerError) {
		super(innerError);
	}
}
