package com.magnetron.billing.service;

import com.magnetron.billing.enumeration.DomainName;
import com.magnetron.billing.enumeration.InnerError;
import com.magnetron.billing.repository.IPersonRepo;
import com.magnetron.billing.repository.entity.Person;
import com.magnetron.billing.service.dto.PersonDto;
import com.magnetron.billing.service.exception.IncompleteDataRequiredException;
import com.magnetron.billing.service.exception.RecordNotFoundException;
import com.magnetron.billing.util.DataMapper;
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

public class PersonServiceTest {

    @Mock
    private IPersonRepo personRepo;

    private ModelMapper modelMapper;

    @InjectMocks
    private PersonService service;

    private final int SIZE = 10;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
        service.setMapper(new ModelMapper());
    }

    @Test
    void canGetAllPerson() {
        // given
        List<?> peopleDto = EntityFactory.getList(DomainName.BillDetail, SIZE);
        List<Person> people = DataMapper.mapList(peopleDto, Person.class);

        // when
        when(personRepo.findAll())
                .thenReturn(people);
        List<?> result = service.getAll();

        // then
        assertThat(result)
                .isNotNull();
        assertThat(result.size())
                .isEqualTo(SIZE);
        verify(personRepo, times(1))
                .findAll();
    }

    @Test
    void shouldCreateNewPerson() {
        // given
        PersonDto personDto = (PersonDto) EntityFactory.create(DomainName.Person);
        ModelMapper mapper = new ModelMapper();
        Person person = mapper.map(personDto, Person.class);
        when(personRepo.save(any(Person.class)))
                .thenReturn(person);

        // when
        service.create(personDto);

        // then
        ArgumentCaptor<Person> personArgumentCaptor = ArgumentCaptor.forClass(Person.class);
        verify(personRepo).save(personArgumentCaptor.capture());
        Person innerPerson = personArgumentCaptor.getValue();
        assertThat(innerPerson)
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
    void shouldFailWhenPersonDoesNotExist(){
        // when
        when(personRepo.findById(anyLong()))
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
    void shouldReturnWhenPersonIsFound(){
        // given
        PersonDto personDto = (PersonDto) EntityFactory.create(DomainName.Person);
        ModelMapper mapper = new ModelMapper();
        Person person = mapper.map(personDto, Person.class);

        // when
        when(personRepo.findById(anyLong()))
                .thenReturn(Optional.of(person));
        PersonDto resultDto = service.getById(1L);

        // then
        assertThat(resultDto)
                .isEqualTo(personDto);
    }

    @Test
    void shouldFailWhenIdIsNullAndTryToUpdatePerson(){
        // when
        IncompleteDataRequiredException exception
                = assertThrows(IncompleteDataRequiredException.class, ()->{
            service.update(null, new PersonDto());
        });

        // then
        assertThat(exception.getStatus())
                .isEqualTo(InnerError.LOST_IDENTIFIER);
    }

    @Test
    void shouldFailWhenTryToUpdateButPersonDoesNotExist(){
        // when
        when(personRepo.findById(anyLong()))
                .thenReturn(Optional.empty());

        RecordNotFoundException exception
                = assertThrows(RecordNotFoundException.class, ()->{
            service.update(1L, new PersonDto());
        });

        // then
        assertThat(exception.getStatus())
                .isEqualTo(InnerError.RECORD_NOT_FOUND);
    }

    @Test
    void shouldUpdatePerson(){
        // given
        PersonDto personDto = (PersonDto) EntityFactory.create(DomainName.Person);
        ModelMapper mapper = new ModelMapper();
        Person person = mapper.map(personDto, Person.class);

        // when
        when(personRepo.findById(anyLong()))
                .thenReturn(Optional.of(person));

        person.setName("Daniel");
        when(personRepo.save(person))
                .thenReturn(person);

        personDto = service.update(1L, personDto);

        // then
        assertThat(personDto.getName())
                .isEqualTo(person.getName());
    }

    @Test
    void shouldFailWhenIdIsNullAndTryToDeletePerson(){
        IncompleteDataRequiredException exception
                = assertThrows(IncompleteDataRequiredException.class, ()->{
            service.delete(null);
        });

        // then
        assertThat(exception.getStatus())
                .isEqualTo(InnerError.LOST_IDENTIFIER);
    }

    @Test
    void shouldFailWhenIdIsNotFoundAndTryToDeletePerson(){
        // when
        when(personRepo.findById(anyLong()))
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
    void shouldDeletePerson(){
        // given
        PersonDto personDto = (PersonDto) EntityFactory.create(DomainName.Person);
        ModelMapper mapper = new ModelMapper();
        Person person = mapper.map(personDto, Person.class);

        // when
        when(personRepo.findById(anyLong()))
                .thenReturn(Optional.of(person));

        boolean result = service.delete(1L);

        // then
        assertThat(result)
                .isEqualTo(true);

        verify(personRepo, times(1))
                .deleteById(anyLong());
    }

}
