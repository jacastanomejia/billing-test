package com.magnetron.billing.service;

import com.magnetron.billing.enumeration.DocumentType;
import com.magnetron.billing.enumeration.InnerError;
import com.magnetron.billing.repository.IPersonRepo;
import com.magnetron.billing.repository.entity.Person;
import com.magnetron.billing.service.dto.PersonDto;
import com.magnetron.billing.service.exception.IncompleteDataRequiredException;
import com.magnetron.billing.service.exception.RecordNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class PersonServiceTest {

    @Mock
    private IPersonRepo iPersonRepo;

    private ModelMapper modelMapper;

    @InjectMocks
    private PersonService service;


    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
        service.setMapper(new ModelMapper());
    }

    @Test
    void canGetAllPerson() {
        // when
        service.getAll();

        // then
        verify(iPersonRepo).findAll();
    }

    @Test
    void shouldCreateNewPerson() {
        // given
        PersonDto personDto = createdPersonDtoInstance();
        ModelMapper mapper = new ModelMapper();
        Person person = mapper.map(personDto, Person.class);
        when(iPersonRepo.save(any(Person.class)))
                .thenReturn(person);

        // when
        service.create(personDto);

        // then
        ArgumentCaptor<Person> personArgumentCaptor = ArgumentCaptor.forClass(Person.class);
        verify(iPersonRepo).save(personArgumentCaptor.capture());
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
        when(iPersonRepo.findById(anyLong()))
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
        PersonDto personDto = createdPersonDtoInstance();
        ModelMapper mapper = new ModelMapper();
        Person person = mapper.map(personDto, Person.class);

        // when
        when(iPersonRepo.findById(anyLong()))
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
        when(iPersonRepo.findById(anyLong()))
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
        PersonDto personDto = createdPersonDtoInstanceBySetter();
        ModelMapper mapper = new ModelMapper();
        Person person = mapper.map(personDto, Person.class);

        // when
        when(iPersonRepo.findById(anyLong()))
                .thenReturn(Optional.of(person));

        person.setName("Daniel");
        when(iPersonRepo.save(person))
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
        when(iPersonRepo.findById(anyLong()))
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
        PersonDto personDto = createdPersonDtoInstanceBySetter();
        ModelMapper mapper = new ModelMapper();
        Person person = mapper.map(personDto, Person.class);

        // when
        when(iPersonRepo.findById(anyLong()))
                .thenReturn(Optional.of(person));

        boolean result = service.delete(1L);

        // then
        assertThat(result)
                .isEqualTo(true);

        verify(iPersonRepo, times(1))
                .deleteById(anyLong());
    }

    private PersonDto createdPersonDtoInstance(){
        return PersonDto.builder()
                .name("Juan")
                .surname("Perez")
                .documentType(DocumentType.CC)
                .documentNumber("2123456")
                .build();
    }

    private PersonDto createdPersonDtoInstanceBySetter(){
        PersonDto personDto = new PersonDto();
        personDto.setName("Pedro");
        personDto.setSurname("Medina");
        personDto.setDocumentType(DocumentType.CC);
        personDto.setDocumentNumber("542568");
        return personDto;
    }
}
