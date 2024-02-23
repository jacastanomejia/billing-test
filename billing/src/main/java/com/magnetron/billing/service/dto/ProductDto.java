package com.magnetron.billing.service.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto implements Serializable {

	@Serial
	private static final long serialVersionUID = 5703463020518028034L;
	
	private Long id;
	
	@NotNull(message="{message.product.description.required}")
	@Size(max=500, message = "{message.product.description.max}")
	private String description;

	@NotNull(message="{message.product.price.required}")
	@Min(0)
	private Double price;

	@NotNull(message="{message.product.cost.required}")
	@Min(0)
	private Double cost;
	
	@NotNull(message="{message.product.unit.required}")
	private String unit;

}
