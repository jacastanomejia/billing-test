package com.magnetron.billing.service;

import com.magnetron.billing.enumeration.InnerError;
import com.magnetron.billing.repository.IProductRepo;
import com.magnetron.billing.repository.entity.Product;
import com.magnetron.billing.service.dto.ProductDto;
import com.magnetron.billing.service.exception.IncompleteDataRequiredException;
import com.magnetron.billing.service.exception.RecordNotFoundException;
import com.magnetron.billing.service.exception.ReferenceNotFoundException;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.test.context.event.annotation.BeforeTestClass;
import org.springframework.test.context.event.annotation.BeforeTestExecution;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class ProductServiceTest {

    @Mock
    private IProductRepo iProductRepo;

    private ModelMapper modelMapper;

    @InjectMocks
    private ProductService service;


    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
        service.setMapper(new ModelMapper());
    }

    @Test
    void canGetAllProduct() {
        // when
        service.getAll();

        // then
        verify(iProductRepo).findAll();
    }

    @Test
    void shouldCreateNewProduct() {
        // given
        ProductDto productDto = createdProductDtoInstance();
        ModelMapper mapper = new ModelMapper();
        Product product = mapper.map(productDto, Product.class);
        when(iProductRepo.save(any(Product.class)))
                .thenReturn(product);

        // when
        service.create(productDto);

        // then
        ArgumentCaptor<Product> productArgumentCaptor = ArgumentCaptor.forClass(Product.class);
        verify(iProductRepo).save(productArgumentCaptor.capture());
        Product innerProduct = productArgumentCaptor.getValue();
        assertThat(innerProduct)
                .isNotNull();

    }

    @Test
    void shouldFailGetById() throws Exception{
        // when
        IncompleteDataRequiredException exception
                = assertThrows(IncompleteDataRequiredException.class, () -> {
            service.getById(null);
        });

        // then
        assertThat(exception.getStatus())
                .isEqualTo(InnerError.LOST_IDENTIFIER);
    }

    @Test
    void shouldFailWhenProductDoesNotExist(){
        // when
        when(iProductRepo.findById(anyLong()))
                .thenReturn(Optional.empty());

        RecordNotFoundException exception
                = assertThrows(RecordNotFoundException.class, ()->{
            service.getById(1L);
        });

        // then
        assertThat(exception.getStatus())
                .isEqualTo(InnerError.RECORD_NOT_FOUND);
    }

    @Test
    void shouldReturnWhenProductIsFound(){
        // given
        ProductDto productDto = createdProductDtoInstance();
        ModelMapper mapper = new ModelMapper();
        Product product = mapper.map(productDto, Product.class);

        // when
        when(iProductRepo.findById(anyLong()))
                .thenReturn(Optional.of(product));
        ProductDto resultDto = service.getById(1L);

        // then
        assertThat(resultDto)
                .isEqualTo(productDto);
    }

    @Test
    void shouldFailWhenIdIsNullAndTryToUpdateProduct(){
        // when
        IncompleteDataRequiredException exception
                = assertThrows(IncompleteDataRequiredException.class, ()->{
            service.update(null, new ProductDto());
        });

        // then
        assertThat(exception.getStatus())
                .isEqualTo(InnerError.LOST_IDENTIFIER);
    }

    @Test
    void shouldFailWhenTryToUpdateButProductDoesNotExist(){
        // when
        when(iProductRepo.findById(anyLong()))
                .thenReturn(Optional.empty());

        RecordNotFoundException exception
                = assertThrows(RecordNotFoundException.class, ()->{
            service.update(1L, new ProductDto());
        });

        // then
        assertThat(exception.getStatus())
                .isEqualTo(InnerError.RECORD_NOT_FOUND);
    }

    @Test
    void shouldUpdateProduct(){
        // given
        ProductDto productDto = createdProductDtoInstanceBySetter();
        ModelMapper mapper = new ModelMapper();
        Product product = mapper.map(productDto, Product.class);

        // when
        when(iProductRepo.findById(anyLong()))
                .thenReturn(Optional.of(product));

        product.setUnit("Cambio");
        when(iProductRepo.save(product))
                .thenReturn(product);

        productDto = service.update(1L, productDto);

        // then
        assertThat(productDto.getUnit())
                .isEqualTo(product.getUnit());
    }

    @Test
    void shouldFailWhenIdIsNullAndTryToDeleteProduct(){
        IncompleteDataRequiredException exception
                = assertThrows(IncompleteDataRequiredException.class, ()->{
            service.delete(null);
        });

        // then
        assertThat(exception.getStatus())
                .isEqualTo(InnerError.LOST_IDENTIFIER);
    }

    @Test
    void shouldFailWhenIdIsNotFoundAndTryToDeleteProduct(){
        // when
        when(iProductRepo.findById(anyLong()))
                .thenReturn(Optional.empty());

        // when
        RecordNotFoundException exception
                = assertThrows(RecordNotFoundException.class, ()->{
            service.delete(1L);
        });

        // then
        assertThat(exception.getStatus())
                .isEqualTo(InnerError.RECORD_NOT_FOUND);
    }

    @Test
    void shouldDeleteProduct(){
        // given
        ProductDto productDto = createdProductDtoInstanceBySetter();
        ModelMapper mapper = new ModelMapper();
        Product product = mapper.map(productDto, Product.class);

        // when
        when(iProductRepo.findById(anyLong()))
                .thenReturn(Optional.of(product));

        boolean result = service.delete(1L);

        // then
        assertThat(result)
                .isEqualTo(true);

        verify(iProductRepo, times(1))
                .deleteById(anyLong());
    }

    private ProductDto createdProductDtoInstance(){
        return ProductDto.builder()
                .description("Shoes")
                .price(21_000d)
                .cost(18_000d)
                .unit("Unidad")
                .build();
    }

    private ProductDto createdProductDtoInstanceBySetter(){
        ProductDto productDto = new ProductDto();
        productDto.setDescription("Item 1");
        productDto.setPrice(22_000d);
        productDto.setCost(12_000d);
        productDto.setUnit("Unidad");

        return productDto;
    }
}
