package com.magnetron.billing.repository.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "fact_detalle")
public class BillDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fdet_id")
    private Long id;

    @Column(name = "fdet_linea")
    @NotEmpty
    @Size(max = 250)
    private String line;

    @Column(name = "fdet_cantidad")
    @NotEmpty
    private Integer quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "z_prod_id")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "z_fenc_id")
    private BillHeader billHeader;
}
