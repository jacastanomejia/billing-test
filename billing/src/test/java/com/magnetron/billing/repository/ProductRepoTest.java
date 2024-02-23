package com.magnetron.billing.repository;

import com.magnetron.billing.repository.entity.Product;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

@DataJpaTest
public class ProductRepoTest {
    @Autowired
    private IProductRepo iProductRepo;

    @AfterEach
    void tearDown(){
        iProductRepo.deleteAll();
    }

    @Test
    void itShouldHaveAProductWithStock(){

        Product product = createdInstance();
        iProductRepo.save(product);
        Optional<Product> p2 = iProductRepo.findById(product.getId());
        Assertions.assertThat(p2.isEmpty()).isFalse();

    }

    private Product createdInstance(){
        return Product.builder()
                .description("Transformador de Potencia 500 kVA'")
                .price(21_000_000d)
                .cost(16_000_000d)
                .unit("Unidad")
                .build();
    }
}
