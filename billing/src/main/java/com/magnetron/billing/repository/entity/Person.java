package com.magnetron.billing.repository.entity;

import com.magnetron.billing.enumeration.DocumentType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
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
@Table(name = "persona")
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "per_id")
    private Long id;

    @Column(name = "per_nombre")
    @NotEmpty
    @Size(max = 50)
    private String name;

    @Column(name = "per_apellido")
    @NotEmpty
    @Size(max = 50)
    private String surname;

    @Column(name = "per_tipo_documento")
    @Enumerated(EnumType.STRING)
    private DocumentType documentType;

    @Column(name = "per_documento")
    @NotEmpty
    @Size(max = 20)
    private String documentNumber;

    @OneToMany(mappedBy = "person", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<BillHeader> billHeaders;

}
