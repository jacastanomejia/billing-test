package com.magnetron.billing.service;


import com.magnetron.billing.enumeration.DomainName;
import com.magnetron.billing.enumeration.InnerError;
import com.magnetron.billing.exception.ApiRestRuntimeException;
import com.magnetron.billing.repository.IBillDetailRepo;
import com.magnetron.billing.repository.IBillHeaderRepo;
import com.magnetron.billing.repository.IProductRepo;
import com.magnetron.billing.repository.entity.BillDetail;
import com.magnetron.billing.repository.entity.BillHeader;
import com.magnetron.billing.repository.entity.Product;
import com.magnetron.billing.service.contract.IBillDetailService;
import com.magnetron.billing.service.dto.BillDetailDto;
import com.magnetron.billing.service.dto.BillDetailFullDto;
import com.magnetron.billing.service.dto.ProductDto;
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
@Qualifier("billDetailService")
public class BillDetailService implements IBillDetailService {

	@Autowired
	private IBillDetailRepo billDetailRepo;

	@Autowired
	private IBillHeaderRepo billHeaderRepo;

	@Autowired
	private IProductRepo productRepo;

	private ModelMapper mapper;

	/**
	 * Obtiene todos los detalles de factura.
	 *
	 * @return Lista de objetos BillDetailDto.
	 * @throws ApiRestRuntimeException Si ocurre un error durante la ejecución.
	 */
	@Override
	public List<BillDetailDto> getAll()
			throws ApiRestRuntimeException {
		return DataMapper.mapList(billDetailRepo.findAll(), BillDetailDto.class);
	}

	/**
	 * Obtiene un detalle de factura por su ID con detalles completos.
	 *
	 * @param id Identificador único del detalle de factura.
	 * @return Objeto BillDetailFullDto correspondiente al ID proporcionado.
	 * @throws ApiRestRuntimeException Si ocurre un error durante la ejecución o si el detalle de factura no se encuentra.
	 */
	@Override
	public BillDetailFullDto getById(Long id)
			throws ApiRestRuntimeException {
		if(id == null)
			throw new IncompleteDataRequiredException(InnerError.LOST_IDENTIFIER);

		Optional<BillDetail> billDetailOptional = billDetailRepo.findById(id);
		if(billDetailOptional.isEmpty())
			throw new RecordNotFoundException(InnerError.RECORD_NOT_FOUND, DomainName.BillDetail.getMessage());

		BillDetail billDetail = billDetailOptional.get();
		BillDetailFullDto billDetailDto = mapper.map(billDetail, BillDetailFullDto.class);
		billDetailDto.setProductDto(mapper.map(billDetail.getProduct(), ProductDto.class));
		return billDetailDto;
	}

	/**
	 * Crea un nuevo detalle de factura.
	 *
	 * @param data Datos del detalle de factura a crear.
	 * @return Objeto BillDetailDto del detalle de factura recién creado.
	 * @throws ApiRestRuntimeException Si ocurre un error durante la ejecución.
	 */
	@Override
	public BillDetailDto create(BillDetailDto data)
			throws ApiRestRuntimeException {

		// Checar que exista el producto referenciado en la factura
		Optional<Product> product = productRepo.findById(data.getIdProduct());
		if(product.isEmpty())
			throw new ReferenceNotFoundException(InnerError.REFERENCE_NOT_FOUND, Product.class.toString());

		// Checar que exista la cabecera de factura
		Optional<BillHeader> billHeader = billHeaderRepo.findById(data.getIdBillHeader());
		if(billHeader.isEmpty())
			throw new ReferenceNotFoundException(InnerError.REFERENCE_NOT_FOUND, BillHeader.class.toString());

		BillDetail billDetail = mapper.map(data, BillDetail.class);
		billDetail.setProduct(product.get());
		billDetail = billDetailRepo.save(billDetail);
		return mapper.map(billDetail, BillDetailDto.class);
	}

	/**
	 * Actualiza un detalle de factura existente por su ID.
	 *
	 * @param id   Identificador único del detalle de factura a actualizar.
	 * @param data Nuevos datos del detalle de factura.
	 * @return Objeto BillDetailDto del detalle de factura actualizado.
	 * @throws ApiRestRuntimeException Si ocurre un error durante la ejecución o si el detalle de factura no se encuentra.
	 */
	@Override
	public BillDetailDto update(Long id, BillDetailDto data)
			throws ApiRestRuntimeException {
		if(id == null)
			throw new IncompleteDataRequiredException(InnerError.LOST_IDENTIFIER);

		// Checar si existe la factura a modificar
		Optional<BillDetail> oldBillDetail = billDetailRepo.findById(id);
		if(oldBillDetail.isEmpty())
			throw new RecordNotFoundException(InnerError.RECORD_NOT_FOUND, DomainName.BillDetail.getMessage());

		// Checar que exista el producto referenciado en la factura
		Optional<Product> product = productRepo.findById(data.getIdProduct());
		if(product.isEmpty())
			throw new ReferenceNotFoundException(InnerError.REFERENCE_NOT_FOUND, Product.class.toString());

		// Checar que exista la cabecera de factura
		Optional<BillHeader> billHeader = billHeaderRepo.findById(data.getIdBillHeader());
		if(billHeader.isEmpty())
			throw new ReferenceNotFoundException(InnerError.REFERENCE_NOT_FOUND, BillHeader.class.toString());

		ModelMapper modelMapper = new ModelMapper();
		BillDetail billDetail = oldBillDetail.get();
		modelMapper.map(data, billDetail);
		billDetail.setProduct(product.get());
		billDetail.setBillHeader(billHeader.get());
		billDetail = billDetailRepo.save(billDetail);
		return mapper.map(billDetail, BillDetailDto.class);
	}

	/**
	 * Elimina un detalle de factura por su ID.
	 *
	 * @param id Identificador único del detalle de factura a eliminar.
	 * @return true si la eliminación fue exitosa.
	 * @throws ApiRestRuntimeException Si ocurre un error durante la ejecución o si el detalle de factura no se encuentra.
	 */
	@Override
	public boolean delete(Long id)
			throws ApiRestRuntimeException {
		if(id == null)
			throw new IncompleteDataRequiredException(InnerError.LOST_IDENTIFIER);

		Optional<BillDetail> billDetail = billDetailRepo.findById(id);
		if(billDetail.isEmpty())
			throw new RecordNotFoundException(InnerError.RECORD_NOT_FOUND, DomainName.BillDetail.getMessage());

		billDetailRepo.deleteById(id);
		return true;
	}

	/**
	 * Obtiene todos los detalles de factura asociados a una factura específica.
	 *
	 * @param id Identificador único de la factura.
	 * @return Lista de objetos BillDetailDto asociados a la factura.
	 * @throws ApiRestRuntimeException Si ocurre un error durante la ejecución o si la factura no se encuentra.
	 */
	@Override
	public List<BillDetailDto> getByReference(Long id)
			throws ApiRestRuntimeException {
		if(id == null)
			throw new IncompleteDataRequiredException(InnerError.LOST_IDENTIFIER);

		Optional<BillHeader> billHeader = billHeaderRepo.findById(id);
		if(billHeader.isEmpty())
			throw new RecordNotFoundException(InnerError.RECORD_NOT_FOUND, DomainName.BillDetail.getMessage());

		return DataMapper.mapList(billDetailRepo.findByBillHeader(billHeader.get()), BillDetailDto.class);
	}

	@Autowired
	public void setMapper(ModelMapper mapper){
		this.mapper = mapper;
	}

}
