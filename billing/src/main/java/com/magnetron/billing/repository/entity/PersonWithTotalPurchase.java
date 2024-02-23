package com.magnetron.billing.repository.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Immutable;

@Entity(name = "vista_total_ventas")
@Immutable
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PersonWithTotalPurchase {
    @Id
    @Column(name = "per_id")
    private Long id;

    @Column(name = "per_nombre")
    private String name;

    @Column(name = "per_apellido")
    private String surname;

    @Column(name = "per_tipo_documento")
    private String documentType;

    @Column(name = "per_documento")
    private String documentNumber;

    @Column(name = "per_total")
    private Double total;
}
