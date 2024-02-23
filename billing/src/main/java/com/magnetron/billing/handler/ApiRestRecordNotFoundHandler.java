package com.magnetron.billing.handler;

import com.magnetron.billing.service.exception.RecordNotFoundException;
import com.magnetron.billing.service.exception.ReferenceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ApiRestRecordNotFoundHandler {

    @ExceptionHandler(value = {
            RecordNotFoundException.class
    })
    public ResponseEntity<Object> handlerApiRequestException(RecordNotFoundException e){
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("element", e.getElementName());
        map.put("status", e.getStatus());
        map.put("message", e.getMessage());

        return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
    }
}
