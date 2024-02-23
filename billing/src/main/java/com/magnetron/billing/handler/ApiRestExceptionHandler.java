package com.magnetron.billing.handler;

import com.magnetron.billing.controller.exception.DataValidationException;
import com.magnetron.billing.exception.ApiRestRuntimeException;
import com.magnetron.billing.service.exception.IncompleteDataRequiredException;
import com.magnetron.billing.service.exception.RecordNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ApiRestExceptionHandler {

    @ExceptionHandler(value = {
            IncompleteDataRequiredException.class,
            DataValidationException.class
    })
    public ResponseEntity<Object> handlerApiRequestException(ApiRestRuntimeException e){
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("type", e.getClass().getCanonicalName());
        map.put("status", e.getStatus());
        map.put("message", e.getMessage());

        return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
    }
}
