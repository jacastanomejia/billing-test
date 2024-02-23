package com.magnetron.billing.service.exception;

import com.magnetron.billing.enumeration.InnerError;
import com.magnetron.billing.exception.ApiRestRuntimeException;

import java.io.Serial;

public class ReferenceNotFoundException extends ApiRestRuntimeException {

	@Serial
	private static final long serialVersionUID = -8918136139277701844L;

	private final String referenceName;

	public ReferenceNotFoundException(InnerError innerError, String referenceName) {
		super(innerError, "No se encuentra el objeto referido en la solicitud");
		this.referenceName = referenceName;
	}

	public String getReferenceName() {
		return referenceName;
	}
}
