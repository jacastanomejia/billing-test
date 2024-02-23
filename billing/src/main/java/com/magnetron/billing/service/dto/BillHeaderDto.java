package com.magnetron.billing.service.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BillHeaderDto implements Serializable {

	@Serial
	private static final long serialVersionUID = 5703463020518028034L;
	
	private Long id;

	@NotNull(message="{message.bill.header.number.required}")
	@Min(0)
	private Integer number;

	@PastOrPresent(message="{message.bill.header.date.required}")
	private LocalDateTime date;

	@NotNull(message="{message.bill.header.person.required}")
	private Long idPerson;

}