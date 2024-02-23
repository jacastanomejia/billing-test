package com.magnetron.billing.repository;

import com.magnetron.billing.repository.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IProductRepo extends JpaRepository<Product, Long> {

}
