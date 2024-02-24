package com.magnetron.billing.service;

import com.magnetron.billing.enumeration.DocumentType;
import com.magnetron.billing.enumeration.DomainName;
import com.magnetron.billing.enumeration.InnerError;
import com.magnetron.billing.repository.IBillHeaderRepo;
import com.magnetron.billing.repository.IPersonRepo;
import com.magnetron.billing.repository.entity.BillHeader;
import com.magnetron.billing.repository.entity.Person;
import com.magnetron.billing.service.dto.BillHeaderDto;
import com.magnetron.billing.service.dto.PersonDto;
import com.magnetron.billing.service.exception.IncompleteDataRequiredException;
import com.magnetron.billing.service.exception.RecordNotFoundException;
import com.magnetron.billing.service.exception.ReferenceNotFoundException;
import com.magnetron.billing.util.EntityFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class BillHeaderServiceTest {

    @Mock
    private IBillHeaderRepo billHeaderRepo;
    
    @Mock
    private IPersonRepo iPersonRepo;

    private ModelMapper modelMapper;

    @InjectMocks
    private BillHeaderService service;


    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
        service.setMapper(new ModelMapper());
    }

    @Test
    void canGetAllBillHeader() {
        // when
        service.getAll();

        // then
        verify(billHeaderRepo).findAll();
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
    void shouldFailWhenBillHeaderDoesNotExist(){
        // when
        when(billHeaderRepo.findById(anyLong()))
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
    void shouldReturnWhenBillHeaderIsFound(){
        // given
        ModelMapper mapper = new ModelMapper();
        PersonDto personDto = (PersonDto) EntityFactory.create(DomainName.Person);
        Person person = mapper.map(personDto, Person.class);

        BillHeaderDto billHeaderDto = (BillHeaderDto) EntityFactory.create(DomainName.BillHeader);
        BillHeader billHeader = mapper.map(billHeaderDto, BillHeader.class);
        billHeader.setBillDetails(List.of());
        billHeader.setPerson(person);

        // when
        when(billHeaderRepo.findById(anyLong()))
                .thenReturn(Optional.of(billHeader));
        BillHeaderDto resultDto = service.getById(1L);

        // then
        assertThat(resultDto)
                .isEqualTo(billHeaderDto);
    }

    @Test
    void shouldFailWhenTryToCreateWithoutPersonReference() throws Exception{
        // when
        when(iPersonRepo.findById(anyLong()))
                .thenReturn(Optional.empty());

        ReferenceNotFoundException exception
                = assertThrows(ReferenceNotFoundException.class, () -> {
            service.create((BillHeaderDto) EntityFactory.create(DomainName.BillHeader));
        });

        // then
        assertThat(exception.getStatus())
                .isEqualTo(InnerError.REFERENCE_NOT_FOUND);
    }

    @Test
    void shouldCreateNewBillHeader() {
        ModelMapper mapper = new ModelMapper();
        // given
        PersonDto personDto = (PersonDto) EntityFactory.create(DomainName.Person);
        Person person = mapper.map(personDto, Person.class);
        when(iPersonRepo.findById(any()))
                .thenReturn(Optional.of(person));

        BillHeaderDto billHeaderDto = (BillHeaderDto) EntityFactory.create(DomainName.BillHeader);
        BillHeader header = mapper.map(billHeaderDto, BillHeader.class);
        when(billHeaderRepo.save(any(BillHeader.class)))
                .thenReturn(header);

        // when
        service.create(billHeaderDto);

        // then
        ArgumentCaptor<BillHeader> personArgumentCaptor = ArgumentCaptor.forClass(BillHeader.class);
        verify(billHeaderRepo).save(personArgumentCaptor.capture());
        BillHeader innerBillHeader= personArgumentCaptor.getValue();
        assertThat(innerBillHeader)
                .isNotNull();

    }

    @Test
    void shouldFailWhenIdIsNullAndTryToUpdateBillHeader(){
        // when
        IncompleteDataRequiredException exception
                = assertThrows(IncompleteDataRequiredException.class, ()->{
            service.update(null, new BillHeaderDto());
        });

        // then
        assertThat(exception.getStatus())
                .isEqualTo(InnerError.LOST_IDENTIFIER);
    }

    @Test
    void shouldFailWhenTryToUpdateButBillHeaderDoesNotExist(){
        // when
        when(billHeaderRepo.findById(anyLong()))
                .thenReturn(Optional.empty());

        RecordNotFoundException exception
                = assertThrows(RecordNotFoundException.class, ()->{
            service.update(1L, new BillHeaderDto());
        });

        // then
        assertThat(exception.getStatus())
                .isEqualTo(InnerError.RECORD_NOT_FOUND);
    }

    @Test
    void shouldFailWhenTryToUpdateButPersonDoesNotExist(){
        ModelMapper mapper = new ModelMapper();
        BillHeaderDto billHeaderDto = (BillHeaderDto) EntityFactory.create(DomainName.BillHeader);
        BillHeader billHeader = mapper.map(billHeaderDto, BillHeader.class);
        billHeader.setBillDetails(List.of());

        // when
        when(billHeaderRepo.findById(any()))
                .thenReturn(Optional.of(billHeader));

        when(iPersonRepo.findById(any()))
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
    void shouldUpdateBillHeader(){
        // given
        ModelMapper mapper = new ModelMapper();

        BillHeaderDto billHeaderDto = (BillHeaderDto) EntityFactory.create(DomainName.BillHeader);
        BillHeader billHeader = mapper.map(billHeaderDto, BillHeader.class);

        PersonDto personDto = (PersonDto) EntityFactory.create(DomainName.Person);
        Person person = mapper.map(personDto, Person.class);

        // when
        when(billHeaderRepo.findById(anyLong()))
                .thenReturn(Optional.of(billHeader));
        when(iPersonRepo.findById(any()))
                .thenReturn(Optional.of(person));

        billHeader.setNumber(123456);
        when(billHeaderRepo.save(billHeader))
                .thenReturn(billHeader);

        billHeaderDto = service.update(1L, billHeaderDto);

        // then
        assertThat(billHeaderDto.getNumber())
                .isEqualTo(billHeader.getNumber());
    }

    @Test
    void shouldFailWhenIdIsNullAndTryToDeleteBillHeader(){
        IncompleteDataRequiredException exception
                = assertThrows(IncompleteDataRequiredException.class, ()->{
            service.delete(null);
        });

        // then
        assertThat(exception.getStatus())
                .isEqualTo(InnerError.LOST_IDENTIFIER);
    }

    @Test
    void shouldFailWhenIdIsNotFoundAndTryToDeleteBillHeader(){
        // when
        when(billHeaderRepo.findById(anyLong()))
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
    void shouldDeleteBillHeader(){
        // given
        BillHeaderDto billHeaderDto = (BillHeaderDto) EntityFactory.create(DomainName.BillHeader);
        ModelMapper mapper = new ModelMapper();
        BillHeader billHeader = mapper.map(billHeaderDto, BillHeader.class);

        // when
        when(billHeaderRepo.findById(anyLong()))
                .thenReturn(Optional.of(billHeader));

        boolean result = service.delete(1L);

        // then
        assertThat(result)
                .isEqualTo(true);

        verify(billHeaderRepo, times(1))
                .deleteById(anyLong());
    }


}
