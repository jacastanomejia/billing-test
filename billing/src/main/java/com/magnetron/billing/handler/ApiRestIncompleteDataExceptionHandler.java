package com.magnetron.billing.handler;

import com.magnetron.billing.exception.ApiRestRuntimeException;
import com.magnetron.billing.service.exception.IncompleteDataRequiredException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ApiRestIncompleteDataExceptionHandler {

    @ExceptionHandler(value = {
            IncompleteDataRequiredException.class
    })
    public ResponseEntity<Object> handlerApiRequestException(ApiRestRuntimeException e){
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("type", e.getClass().getCanonicalName());
        map.put("status", e.getStatus());
        map.put("message", e.getMessage());

        return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
    }
}
