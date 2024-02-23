package com.magnetron.billing.service;


import com.magnetron.billing.enumeration.DomainName;
import com.magnetron.billing.enumeration.InnerError;
import com.magnetron.billing.exception.ApiRestRuntimeException;
import com.magnetron.billing.repository.IPersonRepo;
import com.magnetron.billing.repository.entity.Person;
import com.magnetron.billing.service.contract.IPersonService;
import com.magnetron.billing.service.dto.PersonDto;
import com.magnetron.billing.service.exception.IncompleteDataRequiredException;
import com.magnetron.billing.service.exception.RecordNotFoundException;
import com.magnetron.billing.util.DataMapper;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@Slf4j
@Qualifier("personService")
public class PersonService implements IPersonService {

	@Autowired
	private IPersonRepo personRepo;

	private ModelMapper mapper;

	/**
	 * Obtiene todos las personas.
	 *
	 * @return Lista de objetos PersonDto.
	 * @throws ApiRestRuntimeException Si ocurre un error durante la ejecución.
	 */
	@Override
	public List<PersonDto> getAll()
			throws ApiRestRuntimeException {
		return DataMapper.mapList(personRepo.findAll(), PersonDto.class);
	}

	/**
	 * Obtiene una persona por su ID.
	 *
	 * @param id Identificador único de la persona.
	 * @return Objeto PersonDto correspondiente al ID proporcionado.
	 * @throws ApiRestRuntimeException Si ocurre un error durante la ejecución o si la persona no se encuentra.
	 */
	@Override
	public PersonDto getById(Long id)
			throws ApiRestRuntimeException {
		if(id == null)
			throw new IncompleteDataRequiredException(InnerError.LOST_IDENTIFIER);

		Optional<Person> person = personRepo.findById(id);
		if(person.isEmpty())
			throw new RecordNotFoundException(InnerError.RECORD_NOT_FOUND, DomainName.Person.getMessage());

		return  mapper.map(person.get(), PersonDto.class);
	}

	/**
	 * Crea una nueva persona.
	 *
	 * @param data Datos de la persona a crear.
	 * @return Objeto PersonDto de la persona recién creada.
	 * @throws ApiRestRuntimeException Si ocurre un error durante la ejecución.
	 */
	@Override
	public PersonDto create(PersonDto data) throws ApiRestRuntimeException {
		// Con el fin de mantener la creación idempotente
		Optional<Person> optionalPerson = personRepo.findByDocumentNumber(data.getDocumentNumber());

		Person person = optionalPerson.orElseGet(() -> {
			Person newPerson = mapper.map(data, Person.class);
			return personRepo.save(newPerson);
		});

		return mapper.map(person, PersonDto.class);
	}

	/**
	 * Actualiza una persona existente por su ID.
	 *
	 * @param id   Identificador único de la persona a actualizar.
	 * @param data Nuevos datos de la persona.
	 * @return Objeto PersonDto de la persona actualizada.
	 * @throws ApiRestRuntimeException Si ocurre un error durante la ejecución o si la persona no se encuentra.
	 */
	@Override
	public PersonDto update(Long id, PersonDto data)
			throws ApiRestRuntimeException {

		if(id == null)
			throw new IncompleteDataRequiredException(InnerError.LOST_IDENTIFIER);

		Optional<Person> oldPerson = personRepo.findById(id);
		if(oldPerson.isEmpty())
			throw new RecordNotFoundException(InnerError.RECORD_NOT_FOUND, DomainName.Person.getMessage());

		ModelMapper modelMapper = new ModelMapper();
		Person person = oldPerson.get();
		modelMapper.map(data, person);
		person = personRepo.save(person);
		return mapper.map(person, PersonDto.class);
	}

	/**
	 * Elimina una persona por su ID.
	 *
	 * @param id Identificador único de la persona a eliminar.
	 * @return true si la eliminación fue exitosa.
	 * @throws ApiRestRuntimeException Si ocurre un error durante la ejecución o si la persona no se encuentra.
	 */
	@Override
	public boolean delete(Long id)
			throws ApiRestRuntimeException {

		if(id == null)
			throw new IncompleteDataRequiredException(InnerError.LOST_IDENTIFIER);

		Optional<Person> person = personRepo.findById(id);
		if(person.isEmpty())
			throw new RecordNotFoundException(InnerError.RECORD_NOT_FOUND, DomainName.Person.getMessage());

		personRepo.deleteById(id);
		return true;
	}

	/**
	 * Establece el ModelMapper utilizado por el servicio.
	 *
	 * @param mapper ModelMapper a establecer.
	 */
	@Autowired
	public void setMapper(ModelMapper mapper){
		this.mapper = mapper;
	}

}
