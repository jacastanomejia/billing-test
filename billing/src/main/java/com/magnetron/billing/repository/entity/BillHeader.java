package com.magnetron.billing.repository.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "fact_encabezado")
public class BillHeader {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fenc_id")
    private Long id;

    @Column(name = "fenc_numero")
    @Positive
    private Integer number;

    @Column(name = "fenc_fecha")
    private LocalDateTime date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "z_per_id")
    private Person person;

    @OneToMany(mappedBy = "billHeader", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<BillDetail> billDetails;
}
