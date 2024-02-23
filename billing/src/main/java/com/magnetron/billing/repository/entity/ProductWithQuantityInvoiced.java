package com.magnetron.billing.repository.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Immutable;

@Entity(name = "vista_cantidad_facturada")
@Immutable
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductWithQuantityInvoiced {

    @Id
    @Column(name = "prod_id")
    private Long id;

    @Column(name = "prod_description")
    private String description;

    @Column(name = "prod_precio")
    private Double price;

    @Column(name = "prod_costo")
    private Double cost;

    @Column(name = "prod_um")
    private String unit;

    @Column(name = "prod_cantidad")
    private Integer quantity;
}
