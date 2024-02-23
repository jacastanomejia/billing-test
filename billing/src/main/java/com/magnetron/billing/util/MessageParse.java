package com.magnetron.billing.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MessageParse {

    public static ResponseEntity<Object> generateResponse(String message, HttpStatus status, Object responseObj) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("message", message);
        map.put("status", status.value());
        map.put("data", responseObj);

        return new ResponseEntity<Object>(map, status);
}

    public static String getErrorMessage(BindingResult bindingResult) {
        List<String> stringList = bindingResult
            .getFieldErrors()
            .stream()
            .map(f -> String.format("%s: %s", f.getField(),  f.getDefaultMessage()))
                .collect(Collectors.toList());

        return String.join(", ", stringList);
    }
}
