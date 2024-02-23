package com.magnetron.billing.service.dto;

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
public class BillDetailDto implements Serializable {

	@Serial
	private static final long serialVersionUID = 5703463020518028034L;
	
	private Long id;

	@NotNull(message="{message.bill.line.required}")
	@Size(max = 250, message="{message.bill.line.size}")
	private String line;

	@NotNull(message="{message.bill.quantity.required}")
	private Integer quantity;

	@NotNull(message="{message.bill.product.required}")
	private Long idProduct;

	@NotNull(message="{message.bill.header.required}")
	private Long idBillHeader;

}
