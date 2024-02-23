package com.magnetron.billing.repository;

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
    void itShouldHaveAProductWithStock(){

        Person person = createdInstance();
        iPersonRepo.save(person);
        Optional<Person> p2 = iPersonRepo.findByDocumentNumber(person.getDocumentNumber());
        Assertions.assertThat(p2.isEmpty()).isFalse();
        Assertions.assertThat(p2.get().getDocumentNumber()).isEqualTo(person.getDocumentNumber());

    }

    private Person createdInstance(){
        return Person.builder()
                .name("Javier Antonio")
                .surname("Medina Sanchez")
                .documentNumber("123456789")
                .documentType("CC")
                .build();
    }
}
