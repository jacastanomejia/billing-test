package com.magnetron.billing.repository.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "producto")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "prod_id")
    private Long id;

    @Column(name = "prod_description")
    @NotEmpty
    @Size(max = 500)
    private String description;

    @Column(name = "prod_precio")
    @Positive
    private Double price;

    @Column(name = "prod_costo")
    @Positive
    private Double cost;

    @Column(name = "prod_um")
    @NotEmpty
    @Size(max = 20)
    private String unit;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<BillDetail> billDetails;
}
