package com.magnetron.billing.service.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BillHeaderFullDto
		extends BillHeaderDto
		implements Serializable {

	@Serial
	private static final long serialVersionUID = 5703463020518028034L;

	private List<BillDetailDto> billDetails;

	private PersonDto personDto;
}
