package com.magnetron.billing.service;

import com.magnetron.billing.enumeration.DocumentType;
import com.magnetron.billing.enumeration.DomainName;
import com.magnetron.billing.enumeration.InnerError;
import com.magnetron.billing.repository.IBillDetailRepo;
import com.magnetron.billing.repository.IBillHeaderRepo;
import com.magnetron.billing.repository.IProductRepo;
import com.magnetron.billing.repository.entity.BillDetail;
import com.magnetron.billing.repository.entity.BillHeader;
import com.magnetron.billing.repository.entity.Person;
import com.magnetron.billing.repository.entity.Product;
import com.magnetron.billing.service.dto.BillDetailDto;
import com.magnetron.billing.service.dto.BillHeaderDto;
import com.magnetron.billing.service.dto.PersonDto;
import com.magnetron.billing.service.dto.ProductDto;
import com.magnetron.billing.service.exception.IncompleteDataRequiredException;
import com.magnetron.billing.service.exception.RecordNotFoundException;
import com.magnetron.billing.service.exception.ReferenceNotFoundException;
import com.magnetron.billing.util.DataMapper;
import com.magnetron.billing.util.EntityFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class BillDetailServiceTest {

    @Mock
    private IBillDetailRepo billDetailRepo;

    @Mock
    private IBillHeaderRepo billHeaderRepo;
    
    @Mock
    private IProductRepo productRepo;

    private ModelMapper modelMapper;

    @InjectMocks
    private BillDetailService service;

    private final int SIZE = 10;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
        modelMapper = new ModelMapper();
        service.setMapper(modelMapper);
    }

    @Test
    void canGetAllBillDetails() {
        // given
        List<?> detailsDto = EntityFactory.getList(DomainName.BillDetail, SIZE);
        List<BillDetail> details = DataMapper.mapList(detailsDto, BillDetail.class);

        // when
        when(billDetailRepo.findAll())
                .thenReturn(details);
        List<?> result = service.getAll();

        // then
        assertThat(result)
                .isNotNull();
        assertThat(result.size())
                .isEqualTo(SIZE);
        verify(billDetailRepo, times(1))
                .findAll();
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
    void shouldFailWhenBillDetailDoesNotExist(){
        // when
        when(billDetailRepo.findById(anyLong()))
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
    void shouldReturnWhenBillDetailIsFound(){
        // given
        ProductDto productDto = (ProductDto) EntityFactory.create(DomainName.Product);
        Product product = modelMapper.map(productDto, Product.class);

        BillHeaderDto billHeaderDto = (BillHeaderDto) EntityFactory.create(DomainName.BillHeader);
        BillHeader billHeader = modelMapper.map(billHeaderDto, BillHeader.class);

        BillDetailDto billDetailDto = (BillDetailDto) EntityFactory.create(DomainName.BillDetail);
        BillDetail billDetail = modelMapper.map(billDetailDto, BillDetail.class);

        billDetail.setBillHeader(billHeader);
        billDetail.setProduct(product);

        // when
        when(billDetailRepo.findById(anyLong()))
                .thenReturn(Optional.of(billDetail));
        BillDetailDto resultDto = service.getById(anyLong());

        // then
        assertThat(resultDto)
                .isEqualTo(billDetailDto);
    }

    @Test
    void shouldFailWhenTryToCreateWithoutProductReference() throws Exception{
        // given
        BillDetailDto billDetailDto = (BillDetailDto) EntityFactory.create(DomainName.BillDetail);

        // when
        when(productRepo.findById(anyLong()))
                .thenReturn(Optional.empty());

        ReferenceNotFoundException exception
                = assertThrows(ReferenceNotFoundException.class, () -> {
            service.create(billDetailDto);
        });

        // then
        assertThat(exception.getStatus())
                .isEqualTo(InnerError.REFERENCE_NOT_FOUND);
    }

    @Test
    void shouldCreateNewBillDetail() {
        BillDetailDto billDetailDto = createdBillDetailDtoInstance();
        BillDetail detail = modelMapper.map(billDetailDto, BillDetail.class);

        BillHeaderDto billHeaderDto = (BillHeaderDto) EntityFactory.create(DomainName.BillHeader);
        BillHeader billHeader = modelMapper.map(billHeaderDto, BillHeader.class);

        ProductDto productDto = (ProductDto) EntityFactory.create(DomainName.Product);
        Product product = modelMapper.map(productDto, Product.class);

        // when
        when(productRepo.findById(any()))
                .thenReturn(Optional.of(product));
        when(billHeaderRepo.findById(any()))
                .thenReturn(Optional.of(billHeader));
        when(billDetailRepo.save(any(BillDetail.class)))
                .thenReturn(detail);

        service.create(billDetailDto);

        // then
        ArgumentCaptor<BillDetail> personArgumentCaptor = ArgumentCaptor.forClass(BillDetail.class);
        verify(billDetailRepo).save(personArgumentCaptor.capture());
        BillDetail innerBillDetail= personArgumentCaptor.getValue();
        assertThat(innerBillDetail)
                .isNotNull();

    }

    @Test
    void shouldFailWhenIdIsNullAndTryToUpdateBillDetail(){
        // when
        IncompleteDataRequiredException exception
                = assertThrows(IncompleteDataRequiredException.class, ()->{
            service.update(null, new BillDetailDto());
        });

        // then
        assertThat(exception.getStatus())
                .isEqualTo(InnerError.LOST_IDENTIFIER);
    }

    @Test
    void shouldFailWhenTryToUpdateButBillDetailDoesNotExist(){
        // when
        when(billDetailRepo.findById(anyLong()))
                .thenReturn(Optional.empty());

        RecordNotFoundException exception
                = assertThrows(RecordNotFoundException.class, ()->{
            service.update(1L, new BillDetailDto());
        });

        // then
        assertThat(exception.getStatus())
                .isEqualTo(InnerError.RECORD_NOT_FOUND);
    }

    @Test
    void shouldFailWhenTryToUpdateButProductDoesNotExist(){
        BillDetailDto billHeaderDto = (BillDetailDto) EntityFactory.create(DomainName.BillDetail);
        BillDetail billHeader = modelMapper.map(billHeaderDto, BillDetail.class);

        // when
        when(billDetailRepo.findById(any()))
                .thenReturn(Optional.of(billHeader));

        when(productRepo.findById(any()))
                .thenReturn(Optional.empty());

        ReferenceNotFoundException exception
                = assertThrows(ReferenceNotFoundException.class, ()->{
            service.update(1L, billHeaderDto);
        });

        // then
        assertThat(exception.getStatus())
                .isEqualTo(InnerError.REFERENCE_NOT_FOUND);
    }

    @Test
    void shouldUpdateBillDetail(){
        // given
        BillDetailDto billDetailDto = (BillDetailDto) EntityFactory.create(DomainName.BillDetail);
        BillDetail billDetail = modelMapper.map(billDetailDto, BillDetail.class);

        BillHeaderDto billHeaderDto = (BillHeaderDto) EntityFactory.create(DomainName.BillHeader);
        BillHeader billHeader = modelMapper.map(billHeaderDto, BillHeader.class);

        ProductDto productDto = (ProductDto) EntityFactory.create(DomainName.Product);
        Product product = modelMapper.map(productDto, Product.class);

        // when
        when(billDetailRepo.findById(anyLong()))
                .thenReturn(Optional.of(billDetail));
        when(productRepo.findById(any()))
                .thenReturn(Optional.of(product));
        when(billHeaderRepo.findById(any()))
                .thenReturn(Optional.of(billHeader));

        billDetail.setLine("Item1");
        when(billDetailRepo.save(billDetail))
                .thenReturn(billDetail);

        billDetailDto = service.update(1L, billDetailDto);

        // then
        assertThat(billDetail.getLine())
                .isEqualTo(billDetail.getLine());
    }

    @Test
    void shouldFailWhenIdIsNullAndTryToDeleteBillDetail(){
        IncompleteDataRequiredException exception
                = assertThrows(IncompleteDataRequiredException.class, ()->{
            service.delete(null);
        });

        // then
        assertThat(exception.getStatus())
                .isEqualTo(InnerError.LOST_IDENTIFIER);
    }

    @Test
    void shouldFailWhenIdIsNotFoundAndTryToDeleteBillDetail(){
        // when
        when(billDetailRepo.findById(anyLong()))
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
    void shouldDeleteBillDetail(){
        // given
        BillDetailDto billDetailDto = (BillDetailDto) EntityFactory.create(DomainName.BillDetail);
        BillDetail billDetail = modelMapper.map(billDetailDto, BillDetail.class);

        // when
        when(billDetailRepo.findById(anyLong()))
                .thenReturn(Optional.of(billDetail));

        boolean result = service.delete(1L);

        // then
        assertThat(result)
                .isEqualTo(true);

        verify(billDetailRepo, times(1))
                .deleteById(anyLong());
    }

    private BillDetailDto createdBillDetailDtoInstance(){
        return BillDetailDto.builder()
                .line("Item1")
                .quantity(20)
                .build();
    }


}
