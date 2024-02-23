package com.magnetron.billing.service.exception;

import com.magnetron.billing.enumeration.InnerError;
import com.magnetron.billing.exception.ApiRestRuntimeException;

import java.io.Serial;

public class IncompleteDataRequiredException extends ApiRestRuntimeException {

	@Serial
	private static final long serialVersionUID = -808292580827285468L;

	public IncompleteDataRequiredException(InnerError innerError) {
		super(innerError);
	}
}
