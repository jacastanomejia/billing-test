package com.magnetron.billing.controller;

import com.magnetron.billing.controller.exception.DataValidationException;
import com.magnetron.billing.enumeration.InnerError;
import com.magnetron.billing.enumeration.StatusMessage;
import com.magnetron.billing.service.contract.IProductService;
import com.magnetron.billing.service.dto.ProductDto;
import com.magnetron.billing.util.MessageParse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@CrossOrigin
public class ProductRestController {

	@Autowired
	private IProductService service;
	
	@GetMapping
	public ResponseEntity<Object> getAll(){
		List<ProductDto> dtoList  = service.getAll();
		return MessageParse.generateResponse(StatusMessage.OK.toString(), HttpStatus.OK, dtoList);
	}
	
	@GetMapping(path="/{id}")
	public ResponseEntity<ProductDto> getById(@PathVariable Long id){
		ProductDto dto = service.getById(id);
		return new ResponseEntity<>(dto, HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<Object> create(@Valid @RequestBody ProductDto data, BindingResult validation) {
		// Check validations defined in ProductDto
		if(validation.hasErrors()) {
			throw new DataValidationException(InnerError.DATA_VALIDATION, MessageParse.getErrorMessage(validation));
		}

		ProductDto result = service.create(data);
		return MessageParse.generateResponse(StatusMessage.OK.toString(), HttpStatus.CREATED, result);
	}

	@PutMapping(value = "/{id}")
	public ResponseEntity<Object> update(@PathVariable Long id, @Valid @RequestBody ProductDto data, BindingResult validation) {
 		if(validation.hasErrors()) {
			throw new DataValidationException(InnerError.DATA_VALIDATION, MessageParse.getErrorMessage(validation));
		}

		ProductDto result = service.update(id, data);
		return MessageParse.generateResponse(StatusMessage.OK.toString(), HttpStatus.CREATED, result);
	}
	
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Object> delete(@PathVariable Long id) {
		boolean result = service.delete(id);
		return MessageParse.generateResponse(StatusMessage.OK.toString(), HttpStatus.OK, result);
	}
	
}
