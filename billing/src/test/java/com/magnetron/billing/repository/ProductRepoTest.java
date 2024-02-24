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
    void shouldFindAProductById(){
        // given two products
        Product product = createdInstanceBuilder();
        iProductRepo.save(createdInstanceBySetter());
        iProductRepo.save(product);

        // when
        Optional<Product> p2 = iProductRepo.findById(2L);

        //then
        Assertions.assertThat(p2.isEmpty()).isFalse();
        Assertions.assertThat(p2.get().getDescription()).isEqualTo(product.getDescription());
        Assertions.assertThat(p2.get().getPrice()).isEqualTo(product.getPrice());
        Assertions.assertThat(p2.get().getCost()).isEqualTo(product.getCost());
        Assertions.assertThat(p2.get().getUnit()).isEqualTo(product.getUnit());
    }

    private Product createdInstanceBuilder(){
        return Product.builder()
                .description("Transformador de Potencia 500 kVA")
                .price(21_000_000d)
                .cost(16_000_000d)
                .unit("Unidad")
                .build();
    }

    private Product createdInstanceBySetter(){
        Product product = new Product();
        product.setDescription("Transformador de Fase 500 kVA");
        product.setCost(8_000_000d);
        product.setPrice(11_000_000d);
        product.setUnit("Unidad");
        return product;
    }
}
