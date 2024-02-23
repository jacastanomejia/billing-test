package com.magnetron.billing.controller.exception;

import com.magnetron.billing.enumeration.InnerError;
import com.magnetron.billing.exception.ApiRestRuntimeException;

public class DataValidationException extends ApiRestRuntimeException {

    public DataValidationException(InnerError innerError, String message) {
        super(innerError, message);
    }
}
