package com.magnetron.billing.controller;

import com.magnetron.billing.controller.exception.DataValidationException;
import com.magnetron.billing.enumeration.InnerError;
import com.magnetron.billing.enumeration.StatusMessage;
import com.magnetron.billing.service.contract.IProductService;
import com.magnetron.billing.service.dto.ProductDto;
import com.magnetron.billing.util.MessageParse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

	@Operation(
			summary = "Obtener todos los productos en base de datos",
			description = "Recuperar la lista existente de productos en base de datos"
	)
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200",
					description = "Lista de productos recuperada con éxito",
					content = @Content(mediaType = "application/json")),
	})
	@GetMapping
	public ResponseEntity<Object> getAll(){
		List<ProductDto> dtoList  = service.getAll();
		return MessageParse.generateResponse(StatusMessage.OK.toString(), HttpStatus.OK, dtoList);
	}

	@Operation(
			summary = "Obtener un producto por su ID",
			description = "Recuperar un producto específico por su identificador único"
	)
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200",
					description = "Producto recuperado con éxito",
					content = @Content(mediaType = "application/json")),
			@ApiResponse(
					responseCode = "404",
					description = "Producto no encontrado",
					content = @Content),
	})
	@GetMapping(path="/{id}")
	public ResponseEntity<ProductDto> getById(@PathVariable Long id){
		ProductDto dto = service.getById(id);
		return new ResponseEntity<>(dto, HttpStatus.OK);
	}

	@Operation(
			summary = "Crear un nuevo producto",
			description = "Crear un nuevo producto con los datos proporcionados"
	)
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "201",
					description = "Producto creado con éxito",
					content = @Content(mediaType = "application/json")),
			@ApiResponse(
					responseCode = "400",
					description = "Datos de entrada inválidos",
					content = @Content),
	})
	@PostMapping
	public ResponseEntity<Object> create(@Valid @RequestBody ProductDto data, BindingResult validation) {
		// Check validations defined in ProductDto
		if(validation.hasErrors()) {
			throw new DataValidationException(InnerError.DATA_VALIDATION, MessageParse.getErrorMessage(validation));
		}

		ProductDto result = service.create(data);
		return MessageParse.generateResponse(StatusMessage.OK.toString(), HttpStatus.CREATED, result);
	}

	@Operation(
			summary = "Actualizar un producto existente por su ID",
			description = "Actualizar un producto existente con los nuevos datos proporcionados"
	)
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "201",
					description = "Producto actualizado con éxito",
					content = @Content(mediaType = "application/json")),
			@ApiResponse(
					responseCode = "400",
					description = "Datos de entrada inválidos",
					content = @Content),
			@ApiResponse(
					responseCode = "404",
					description = "Producto no encontrado",
					content = @Content),
	})
	@PutMapping(value = "/{id}")
	public ResponseEntity<Object> update(@PathVariable Long id, @Valid @RequestBody ProductDto data, BindingResult validation) {
 		if(validation.hasErrors()) {
			throw new DataValidationException(InnerError.DATA_VALIDATION, MessageParse.getErrorMessage(validation));
		}

		ProductDto result = service.update(id, data);
		return MessageParse.generateResponse(StatusMessage.OK.toString(), HttpStatus.CREATED, result);
	}

	@Operation(
			summary = "Eliminar un producto por su ID",
			description = "Eliminar un producto existente por su identificador único"
	)
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200",
					description = "Producto eliminado con éxito",
					content = @Content(mediaType = "application/json")),
			@ApiResponse(
					responseCode = "404",
					description = "Producto no encontrado",
					content = @Content),
	})
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Object> delete(@PathVariable Long id) {
		boolean result = service.delete(id);
		return MessageParse.generateResponse(StatusMessage.OK.toString(), HttpStatus.OK, result);
	}
	
}
