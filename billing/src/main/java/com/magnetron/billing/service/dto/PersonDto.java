package com.magnetron.billing.service.dto;

import com.magnetron.billing.enumeration.DocumentType;
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
public class PersonDto implements Serializable {

	@Serial
	private static final long serialVersionUID = 5703463020518028034L;
	
	private Long id;
	
	@NotNull(message="{message.person.name.required}")
	@Size(max=50, message="{message.person.name.size}")
	private String name;

	@NotNull(message="{message.person.surname.required}")
	@Size(max=50, message="{message.person.surname.size}")
	private String surname;

	@NotNull(message="{message.person.type.required}")
	private DocumentType documentType;

	@NotNull(message="{message.person.number.required}")
	@Size(min=5, max=20, message="{message.person.number.size}")
	private String documentNumber;
	
}
