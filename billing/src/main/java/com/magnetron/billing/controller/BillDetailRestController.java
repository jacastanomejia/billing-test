package com.magnetron.billing.controller;

import com.magnetron.billing.controller.exception.DataValidationException;
import com.magnetron.billing.enumeration.InnerError;
import com.magnetron.billing.enumeration.StatusMessage;
import com.magnetron.billing.service.contract.IBillDetailService;
import com.magnetron.billing.service.dto.BillDetailDto;
import com.magnetron.billing.service.dto.BillDetailFullDto;
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
@RequestMapping("/api/v1/bills/{id}/details")
@CrossOrigin
public class BillDetailRestController {

	@Autowired
	private IBillDetailService service;

	@Operation(
			summary = "Obtener todas las líneas del detalle de la factura",
			description = "Utilizando el número de factura, se puede recuperar el listado de cada artículo comprado."
	)
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200",
					description = "Lista de detalles de factura recuperada con éxito",
					content = @Content(mediaType = "application/json")),
	})
	@GetMapping
	public ResponseEntity<Object> getAll(@PathVariable Long id){
		List<BillDetailDto> dtoList  = service.getByReference(id);
		return MessageParse.generateResponse(StatusMessage.OK.toString(), HttpStatus.OK, dtoList);
	}

	@Operation(
			summary = "Crear nueva linea en detalle de factura",
			description = "Permite el almacenamiento de una linea con detalle de la compra"
	)
	@GetMapping(path="/{detailId}")
	public ResponseEntity<BillDetailFullDto> getById(@PathVariable Long id, @PathVariable Long detailId){
		BillDetailFullDto dto = service.getById(detailId);
		return new ResponseEntity<>(dto, HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<Object> create(@PathVariable Long id, @Valid @RequestBody BillDetailDto data, BindingResult validation) {
		// Check validations defined in BillDetailDto
		if(validation.hasErrors()) {
			throw new DataValidationException(InnerError.DATA_VALIDATION, MessageParse.getErrorMessage(validation));
		}

		data.setIdBillHeader(id);
		BillDetailDto result = service.create(data);
		return MessageParse.generateResponse(StatusMessage.OK.toString(), HttpStatus.CREATED, result);
	}

	@PutMapping(value = "/{detailId}")
	public ResponseEntity<Object> update(@PathVariable Long id, @PathVariable Long detailId, @Valid @RequestBody BillDetailDto data, BindingResult validation) {
 		if(validation.hasErrors()) {
			throw new DataValidationException(InnerError.DATA_VALIDATION, MessageParse.getErrorMessage(validation));
		}

		BillDetailDto result = service.update(detailId, data);
		return MessageParse.generateResponse(StatusMessage.OK.toString(), HttpStatus.CREATED, result);
	}
	
	@DeleteMapping(value = "/{detailId}")
	public ResponseEntity<Object> delete(@PathVariable Long id, @PathVariable Long detailId) {
		boolean result = service.delete(detailId);
		return MessageParse.generateResponse(StatusMessage.OK.toString(), HttpStatus.OK, result);
	}
	
}
