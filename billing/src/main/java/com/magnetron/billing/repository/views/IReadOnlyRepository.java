package com.magnetron.billing.repository.views;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;

import java.util.List;

@NoRepositoryBean
public interface IReadOnlyRepository<T, ID> extends Repository<T, ID> {

    List<T> findAll();

    List<T> findById(ID id);

}
