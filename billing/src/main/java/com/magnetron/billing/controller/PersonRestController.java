package com.magnetron.billing.controller;

import com.magnetron.billing.controller.exception.DataValidationException;
import com.magnetron.billing.enumeration.InnerError;
import com.magnetron.billing.enumeration.StatusMessage;
import com.magnetron.billing.service.contract.IPersonService;
import com.magnetron.billing.service.dto.PersonDto;
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
@RequestMapping("/api/v1/people")
@CrossOrigin
public class PersonRestController {

	@Autowired
	private IPersonService service;

	@Operation(
			summary = "Obtener todas las personas",
			description = "Recuperar la lista existente de personas"
	)
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200",
					description = "Lista de personas recuperada con éxito",
					content = @Content(mediaType = "application/json")),
	})
	@GetMapping
	public ResponseEntity<Object> getAll(){
		List<PersonDto> dtoList  = service.getAll();
		return MessageParse.generateResponse(StatusMessage.OK.toString(), HttpStatus.OK, dtoList);
	}

	@Operation(
			summary = "Obtener una persona por su ID",
			description = "Recuperar una persona específica por su identificador único"
	)
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200",
					description = "Persona recuperada con éxito",
					content = @Content(mediaType = "application/json")),
			@ApiResponse(
					responseCode = "404",
					description = "Persona no encontrada",
					content = @Content),
	})
	@GetMapping(path="/{id}")
	public ResponseEntity<PersonDto> getById(@PathVariable Long id){
		PersonDto dto = service.getById(id);
		return new ResponseEntity<>(dto, HttpStatus.OK);
	}

	@Operation(
			summary = "Crear una nueva persona",
			description = "Crear una nueva persona con los datos proporcionados"
	)
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "201",
					description = "Persona creada con éxito",
					content = @Content(mediaType = "application/json")),
			@ApiResponse(
					responseCode = "400",
					description = "Datos de entrada inválidos",
					content = @Content),
	})
	@PostMapping
	public ResponseEntity<Object> create(@Valid @RequestBody PersonDto data, BindingResult validation) {
		// Check validations defined in PersonDto
		if(validation.hasErrors()) {
			throw new DataValidationException(InnerError.DATA_VALIDATION, MessageParse.getErrorMessage(validation));
		}

		PersonDto result = service.create(data);
		return MessageParse.generateResponse(StatusMessage.OK.toString(), HttpStatus.CREATED, result);
	}

	@Operation(
			summary = "Actualizar una persona existente por su ID",
			description = "Actualizar una persona existente con los nuevos datos proporcionados"
	)
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "201",
					description = "Persona actualizada con éxito",
					content = @Content(mediaType = "application/json")),
			@ApiResponse(
					responseCode = "400",
					description = "Datos de entrada inválidos",
					content = @Content),
			@ApiResponse(
					responseCode = "404",
					description = "Persona no encontrada",
					content = @Content),
	})
	@PutMapping(value = "/{id}")
	public ResponseEntity<Object> update(@PathVariable Long id, @Valid @RequestBody PersonDto data, BindingResult validation) {
 		if(validation.hasErrors()) {
			throw new DataValidationException(InnerError.DATA_VALIDATION, MessageParse.getErrorMessage(validation));
		}

		PersonDto result = service.update(id, data);
		return MessageParse.generateResponse(StatusMessage.OK.toString(), HttpStatus.CREATED, result);
	}

	@Operation(
			summary = "Eliminar una persona por su ID",
			description = "Eliminar una persona existente por su identificador único"
	)
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200",
					description = "Persona eliminada con éxito",
					content = @Content(mediaType = "application/json")),
			@ApiResponse(
					responseCode = "404",
					description = "Persona no encontrada",
					content = @Content),
	})
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Object> delete(@PathVariable Long id) {
		boolean result = service.delete(id);
		return MessageParse.generateResponse(StatusMessage.OK.toString(), HttpStatus.OK, result);
	}
	
}
