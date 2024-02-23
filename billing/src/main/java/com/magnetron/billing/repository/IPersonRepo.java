package com.magnetron.billing.repository;

import com.magnetron.billing.repository.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IPersonRepo extends JpaRepository<Person, Long> {
    public Optional<Person> findByDocumentNumber(String documentNumber);

}
