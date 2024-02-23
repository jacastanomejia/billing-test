package com.magnetron.billing.controller;

import com.magnetron.billing.controller.exception.DataValidationException;
import com.magnetron.billing.enumeration.InnerError;
import com.magnetron.billing.enumeration.StatusMessage;
import com.magnetron.billing.service.contract.IBillHeaderService;
import com.magnetron.billing.service.dto.BillHeaderDto;
import com.magnetron.billing.service.dto.BillHeaderFullDto;
import com.magnetron.billing.util.MessageParse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
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
@RequestMapping("/api/v1/bills")
@CrossOrigin
public class BillHeaderRestController {

	@Autowired
	private IBillHeaderService service;

	@Operation(
			summary = "Obtener todos los encabezados de factura",
			description = "Recupera la lista completa de encabezados de factura."
	)
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200",
					description = "Lista de encabezados de factura recuperada con éxito",
					content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = BillHeaderDto.class))))
	})
	@GetMapping
	public ResponseEntity<Object> getAll(){
		List<BillHeaderDto> dtoList  = service.getAll();
		return MessageParse.generateResponse(StatusMessage.OK.toString(), HttpStatus.OK, dtoList);
	}

	@Operation(
			summary = "Obtener un encabezado de factura por ID",
			description = "Recupera un encabezado de factura específico por su ID con detalles completos."
	)
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200",
					description = "Encabezado de factura recuperado con éxito",
					content = @Content(mediaType = "application/json", schema = @Schema(implementation = BillHeaderFullDto.class))),
			@ApiResponse(
					responseCode = "404",
					description = "Encabezado de factura no encontrado",
					content = @Content)
	})
	@GetMapping(path="/{id}")
	public ResponseEntity<BillHeaderFullDto> getById(@PathVariable Long id){
		BillHeaderFullDto dto = service.getById(id);
		return new ResponseEntity<>(dto, HttpStatus.OK);
	}

	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "201",
					description = "Encabezado de factura creado con éxito",
					content = @Content(mediaType = "application/json", schema = @Schema(implementation = BillHeaderDto.class))),
			@ApiResponse(
					responseCode = "400",
					description = "Error de validación de datos",
					content = @Content),
			@ApiResponse(
					responseCode = "422",
					description = "Error de validación de datos",
					content = @Content)
	})
	@PostMapping
	public ResponseEntity<Object> create(@Valid @RequestBody BillHeaderDto data, BindingResult validation) {
		// Check validations defined in BillHeaderDto
		if(validation.hasErrors()) {
			throw new DataValidationException(InnerError.DATA_VALIDATION, MessageParse.getErrorMessage(validation));
		}

		BillHeaderDto result = service.create(data);
		return MessageParse.generateResponse(StatusMessage.OK.toString(), HttpStatus.CREATED, result);
	}

	@Operation(
			summary = "Actualizar un encabezado de factura existente por ID",
			description = "Actualiza un encabezado de factura existente con los nuevos datos proporcionados."
	)
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "201",
					description = "Encabezado de factura actualizado con éxito",
					content = @Content(mediaType = "application/json", schema = @Schema(implementation = BillHeaderDto.class))),
			@ApiResponse(
					responseCode = "400",
					description = "Error de validación de datos",
					content = @Content),
			@ApiResponse(
					responseCode = "404",
					description = "Encabezado de factura no encontrado",
					content = @Content),
			@ApiResponse(
					responseCode = "422",
					description = "Error de validación de datos",
					content = @Content)
	})
	@PutMapping(value = "/{id}")
	public ResponseEntity<Object> update(@PathVariable Long id, @Valid @RequestBody BillHeaderDto data, BindingResult validation) {
 		if(validation.hasErrors()) {
			throw new DataValidationException(InnerError.DATA_VALIDATION, MessageParse.getErrorMessage(validation));
		}

		BillHeaderDto result = service.update(id, data);
		return MessageParse.generateResponse(StatusMessage.OK.toString(), HttpStatus.CREATED, result);
	}

	@Operation(
			summary = "Eliminar un encabezado de factura por ID",
			description = "Elimina un encabezado de factura existente por su ID."
	)
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200",
					description = "Encabezado de factura eliminado con éxito",
					content = @Content(mediaType = "application/json", schema = @Schema(implementation = Boolean.class))),
			@ApiResponse(
					responseCode = "404",
					description = "Encabezado de factura no encontrado",
					content = @Content)
	})
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Object> delete(@PathVariable Long id) {
		boolean result = service.delete(id);
		return MessageParse.generateResponse(StatusMessage.OK.toString(), HttpStatus.OK, result);
	}
	
}
