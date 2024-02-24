package com.magnetron.billing.service;


import com.magnetron.billing.enumeration.DomainName;
import com.magnetron.billing.enumeration.InnerError;
import com.magnetron.billing.exception.ApiRestRuntimeException;
import com.magnetron.billing.repository.IBillHeaderRepo;
import com.magnetron.billing.repository.IPersonRepo;
import com.magnetron.billing.repository.entity.BillHeader;
import com.magnetron.billing.repository.entity.Person;
import com.magnetron.billing.service.contract.IBillHeaderService;
import com.magnetron.billing.service.dto.BillDetailDto;
import com.magnetron.billing.service.dto.BillHeaderDto;
import com.magnetron.billing.service.dto.BillHeaderFullDto;
import com.magnetron.billing.service.dto.PersonDto;
import com.magnetron.billing.service.exception.IncompleteDataRequiredException;
import com.magnetron.billing.service.exception.RecordNotFoundException;
import com.magnetron.billing.service.exception.ReferenceNotFoundException;
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
@Qualifier("billHeaderService")
public class BillHeaderService implements IBillHeaderService {

	@Autowired
	private IBillHeaderRepo billHeaderRepo;

	@Autowired
	private IPersonRepo personRepo;

	private ModelMapper mapper;

	/**
	 * Obtiene todos los encabezados de factura.
	 *
	 * @return Lista de objetos BillHeaderDto.
	 * @throws ApiRestRuntimeException Si ocurre un error durante la ejecución.
	 */
	@Override
	public List<BillHeaderDto> getAll()
			throws ApiRestRuntimeException {
		return DataMapper.mapList(billHeaderRepo.findAll(), BillHeaderDto.class);
	}

	/**
	 * Obtiene un encabezado de factura por su ID con detalles completos.
	 *
	 * @param id Identificador único del encabezado de factura.
	 * @return Objeto BillHeaderFullDto correspondiente al ID proporcionado.
	 * @throws ApiRestRuntimeException Si ocurre un error durante la ejecución o si el encabezado de factura no se encuentra.
	 */
	@Override
	public BillHeaderFullDto getById(Long id)
			throws ApiRestRuntimeException {
		if(id == null)
			throw new IncompleteDataRequiredException(InnerError.LOST_IDENTIFIER);

		Optional<BillHeader> billHeaderOptional = billHeaderRepo.findById(id);
		if(billHeaderOptional.isEmpty())
			throw new RecordNotFoundException(InnerError.RECORD_NOT_FOUND, DomainName.BillHeader.getMessage());

		BillHeader billHeader = billHeaderOptional.get();
		BillHeaderFullDto billHeaderDto = mapper.map(billHeader, BillHeaderFullDto.class);
		List<BillDetailDto> details = DataMapper.mapList(billHeader.getBillDetails(), BillDetailDto.class);
		billHeaderDto.setBillDetails(details);
		PersonDto buyer = mapper.map(billHeader.getPerson(), PersonDto.class);
		billHeaderDto.setPersonDto(buyer);
		return billHeaderDto;
	}

	/**
	 * Crea un nuevo encabezado de factura.
	 *
	 * @param data Datos del encabezado de factura a crear.
	 * @return Objeto BillHeaderDto del encabezado de factura recién creado.
	 * @throws ApiRestRuntimeException Si ocurre un error durante la ejecución.
	 */
	@Override
	public BillHeaderDto create(BillHeaderDto data)
			throws ApiRestRuntimeException {
		Optional<Person> person = personRepo.findById(data.getIdPerson());
		if(person.isEmpty())
			throw new ReferenceNotFoundException(InnerError.REFERENCE_NOT_FOUND, Person.class.toString());

		BillHeader billHeader = mapper.map(data, BillHeader.class);
		billHeader.setPerson(person.get());
		billHeader = billHeaderRepo.save(billHeader);
		return mapper.map(billHeader, BillHeaderDto.class);
	}

	/**
	 * Actualiza un encabezado de factura existente por su ID.
	 *
	 * @param id   Identificador único del encabezado de factura a actualizar.
	 * @param data Nuevos datos del encabezado de factura.
	 * @return Objeto BillHeaderDto del encabezado de factura actualizado.
	 * @throws ApiRestRuntimeException Si ocurre un error durante la ejecución o si el encabezado de factura no se encuentra.
	 */
	@Override
	public BillHeaderDto update(Long id, BillHeaderDto data)
			throws ApiRestRuntimeException {
		if(id == null)
			throw new IncompleteDataRequiredException(InnerError.LOST_IDENTIFIER);

		Optional<BillHeader> oldBillHeader = billHeaderRepo.findById(id);
		if(oldBillHeader.isEmpty())
			throw new RecordNotFoundException(InnerError.RECORD_NOT_FOUND, DomainName.BillHeader.getMessage());

		Optional<Person> person = personRepo.findById(data.getIdPerson());
		if(person.isEmpty())
			throw new ReferenceNotFoundException(InnerError.REFERENCE_NOT_FOUND, Person.class.toString());

		BillHeader billHeader = oldBillHeader.get();
		billHeader.setNumber(data.getNumber());
		billHeader.setDate(data.getDate());
		billHeader.setPerson(person.get());
		billHeader = billHeaderRepo.save(billHeader);
		return mapper.map(billHeader, BillHeaderDto.class);
	}

	/**
	 * Elimina un encabezado de factura por su ID.
	 *
	 * @param id Identificador único del encabezado de factura a eliminar.
	 * @return true si la eliminación fue exitosa.
	 * @throws ApiRestRuntimeException Si ocurre un error durante la ejecución o si el encabezado de factura no se encuentra.
	 */
	@Override
	public boolean delete(Long id)
			throws ApiRestRuntimeException {
		if(id == null)
			throw new IncompleteDataRequiredException(InnerError.LOST_IDENTIFIER);

		Optional<BillHeader> billHeader = billHeaderRepo.findById(id);
		if(billHeader.isEmpty())
			throw new RecordNotFoundException(InnerError.RECORD_NOT_FOUND, DomainName.BillHeader.getMessage());

		billHeaderRepo.deleteById(id);
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
