package com.magnetron.billing.repository;

import com.magnetron.billing.repository.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IProductRepo extends JpaRepository<Product, Long> {

}
