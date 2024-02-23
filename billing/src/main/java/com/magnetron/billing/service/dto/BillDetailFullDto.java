package com.magnetron.billing.service.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BillDetailFullDto
		extends BillDetailDto
		implements Serializable {

	@Serial
	private static final long serialVersionUID = 5703463020518028034L;

	private ProductDto productDto;
}
