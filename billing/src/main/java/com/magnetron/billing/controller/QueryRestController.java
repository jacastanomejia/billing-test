package com.magnetron.billing.controller;

import com.magnetron.billing.enumeration.StatusMessage;
import com.magnetron.billing.repository.entity.*;
import com.magnetron.billing.service.contract.*;
import com.magnetron.billing.util.MessageParse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/queries")
@CrossOrigin
public class QueryRestController {

	@Autowired
	private ITotalPurchaseByPersonService totalPurchaseService;

	@Autowired
	private IMoreExpensivePurchaseService serviceMostExpensive;

	@Autowired
	private IQuantityInvoicedProductService serviceQuantityInvoiced;

	@Autowired
	private IGrossProfitProductService grossProfitProductService;

	@Autowired
	private IMarginProfitProductService marginProfitProductService;

	@Operation(
			summary = "Personas con total facturado",
			description = "Lista de todas las personas con el total facturado de cada una."
	)
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200",
					description = "Lista recuperada con éxito",
					content = @Content(mediaType = "application/json")),
	})
	@GetMapping(path="/people/total-purchase")
	public ResponseEntity<Object> getAllPeopleWithTotalPurchase(){
		List<PersonWithTotalPurchase> result  = totalPurchaseService.getAll();
		return MessageParse.generateResponse(StatusMessage.OK.toString(), HttpStatus.OK, result);
	}

	@Operation(
			summary = "Personas que han comprado el producto mas costos",
			description = "Lista de todas las personas que han comprado el producto más costoso."
	)
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200",
					description = "Lista recuperada con éxito",
					content = @Content(mediaType = "application/json")),
	})
	@GetMapping(path="/people/more-expensive-purchase")
	public ResponseEntity<Object> getAllPeopleBuyingMostExpensive(){
		List<PersonMoreExpensivePurchase> result  = serviceMostExpensive.getAll();
		return MessageParse.generateResponse(StatusMessage.OK.toString(), HttpStatus.OK, result);
	}

	@Operation(
			summary = "Cantidades facturadas de productos",
			description = "Lista de productos con el total de las cantidades facturadas de cada uno y ordenada"
	)
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200",
					description = "Lista recuperada con éxito",
					content = @Content(mediaType = "application/json")),
	})
	@GetMapping(path="/products/quantity-invoiced")
	public ResponseEntity<Object> getAllProductInvoiced(){
		List<ProductWithQuantityInvoiced> result  = serviceQuantityInvoiced.getAll();
		return MessageParse.generateResponse(StatusMessage.OK.toString(), HttpStatus.OK, result);
	}

	@Operation(
			summary = "Utilidad bruta",
			description = "Lista de productos con la utilidad bruta generada de cada uno"
	)
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200",
					description = "Lista recuperada con éxito",
					content = @Content(mediaType = "application/json")),
	})
	@GetMapping(path="/products/gross-profit")
	public ResponseEntity<Object> getAllProductWithGrossProfit(){
		List<ProductWithGrossProfit> result  = grossProfitProductService.getAll();
		return MessageParse.generateResponse(StatusMessage.OK.toString(), HttpStatus.OK, result);
	}

	@Operation(
			summary = "Margen de utilidad",
			description = "Lista de productos con el margen de utilidad generada de cada uno"
	)
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200",
					description = "Lista recuperada con éxito",
					content = @Content(mediaType = "application/json")),
	})
	@GetMapping(path="/products/margin-profit")
	public ResponseEntity<Object> getAllProductWithMarginProfit(){
		List<ProductWithMarginProfit> result  = marginProfitProductService.getAll();
		return MessageParse.generateResponse(StatusMessage.OK.toString(), HttpStatus.OK, result);
	}
	
}
