package com.magnetron.billing.repository;

import com.magnetron.billing.enumeration.DocumentType;
import com.magnetron.billing.repository.entity.Person;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

@DataJpaTest
public class PersonRepoTest {
    @Autowired
    private IPersonRepo iPersonRepo;

    @AfterEach
    void tearDown(){
        iPersonRepo.deleteAll();
    }

    @Test
    void shouldFindPersonByDocumentNumber(){
        // give two people
        Person person = createdInstanceByBuilder();
        iPersonRepo.save(person);
        iPersonRepo.save(createdInstanceBySetter());

        // when consulted by document number
        Optional<Person> p2 = iPersonRepo.findByDocumentNumber(person.getDocumentNumber());

        // then
        Assertions.assertThat(p2.isEmpty()).isFalse();
        Assertions.assertThat(p2.get().getId()).isNotNull().isPositive();
        Assertions.assertThat(p2.get().getName()).isEqualTo(person.getName());
        Assertions.assertThat(p2.get().getSurname()).isEqualTo(person.getSurname());
        Assertions.assertThat(p2.get().getDocumentType()).isEqualTo(person.getDocumentType());
        Assertions.assertThat(p2.get().getDocumentNumber()).isEqualTo(person.getDocumentNumber());
    }

    private Person createdInstanceByBuilder(){
        return Person.builder()
                .name("Javier Antonio")
                .surname("Medina Sanchez")
                .documentNumber("123456789")
                .documentType(DocumentType.CC)
                .build();
    }

    private Person createdInstanceBySetter(){
        Person person = new Person();
        person.setName("Jesus");
        person.setSurname("Sanchez");
        person.setDocumentType(DocumentType.CE);
        person.setDocumentNumber("4321234");
        return person;
    }
}
