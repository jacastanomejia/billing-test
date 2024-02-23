package com.magnetron.billing.exception;


import com.magnetron.billing.enumeration.InnerError;

import java.io.Serial;

public class ApiRestRuntimeException extends RuntimeException{

	@Serial
	private static final long serialVersionUID = 1436387064490985371L;

	protected InnerError innerError;
	protected String message;

	public ApiRestRuntimeException(InnerError innerError){
		super();
		this.innerError = innerError;
	}

	public ApiRestRuntimeException(InnerError innerError, String message){
		super(message);
		this.innerError = innerError;
	}

	public InnerError getStatus(){
		return innerError;
	}

}
