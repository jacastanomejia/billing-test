package com.magnetron.billing.service;


import com.magnetron.billing.enumeration.DomainName;
import com.magnetron.billing.enumeration.InnerError;
import com.magnetron.billing.exception.ApiRestRuntimeException;
import com.magnetron.billing.repository.IProductRepo;
import com.magnetron.billing.repository.entity.Product;
import com.magnetron.billing.service.contract.IProductService;
import com.magnetron.billing.service.dto.ProductDto;
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
@Qualifier("productService")
public class ProductService implements IProductService {

	@Autowired
	private IProductRepo productRepo;

	private ModelMapper mapper;

	/**
	 * Obtiene todos los productos.
	 *
	 * @return Lista de objetos ProductDto.
	 * @throws ApiRestRuntimeException Si ocurre un error durante la ejecución.
	 */
	@Override
	public List<ProductDto> getAll()
			throws ApiRestRuntimeException {
		return DataMapper.mapList(productRepo.findAll(), ProductDto.class);
	}

	/**
	 * Obtiene un producto por su ID.
	 *
	 * @param id Identificador único del producto.
	 * @return Objeto ProductDto correspondiente al ID proporcionado.
	 * @throws ApiRestRuntimeException Si ocurre un error durante la ejecución o si el producto no se encuentra.
	 */
	@Override
	public ProductDto getById(Long id)
			throws ApiRestRuntimeException {
		if(id == null)
			throw new IncompleteDataRequiredException(InnerError.LOST_IDENTIFIER);

		Optional<Product> product = productRepo.findById(id);
		if(product.isEmpty())
			throw new RecordNotFoundException(InnerError.RECORD_NOT_FOUND, DomainName.Product.getMessage());

		return  mapper.map(product.get(), ProductDto.class);
	}

	/**
	 * Crea un nuevo producto.
	 *
	 * @param data Datos del producto a crear.
	 * @return Objeto ProductDto del producto recién creado.
	 * @throws ApiRestRuntimeException Si ocurre un error durante la ejecución.
	 */
	@Override
	public ProductDto create(ProductDto data)
			throws ApiRestRuntimeException {
		Product product = mapper.map(data, Product.class);
		product = productRepo.save(product);
		return mapper.map(product, ProductDto.class);
	}

	/**
	 * Actualiza un producto existente por su ID.
	 *
	 * @param id   Identificador único del producto a actualizar.
	 * @param data Nuevos datos del producto.
	 * @return Objeto ProductDto del producto actualizado.
	 * @throws ApiRestRuntimeException Si ocurre un error durante la ejecución o si el producto no se encuentra.
	 */
	@Override
	public ProductDto update(Long id, ProductDto data)
			throws ApiRestRuntimeException {
		if(id == null)
			throw new IncompleteDataRequiredException(InnerError.LOST_IDENTIFIER);

		Optional<Product> oldProduct = productRepo.findById(id);
		if(oldProduct.isEmpty())
			throw new RecordNotFoundException(InnerError.RECORD_NOT_FOUND, DomainName.Product.getMessage());

		ModelMapper modelMapper = new ModelMapper();
		Product product = oldProduct.get();
		modelMapper.map(data, product);
		product = productRepo.save(product);
		return mapper.map(product, ProductDto.class);
	}

	/**
	 * Elimina un producto por su ID.
	 *
	 * @param id Identificador único del producto a eliminar.
	 * @return true si la eliminación fue exitosa.
	 * @throws ApiRestRuntimeException Si ocurre un error durante la ejecución o si el producto no se encuentra.
	 */
	@Override
	public boolean delete(Long id)
			throws ApiRestRuntimeException {
		if(id == null)
			throw new IncompleteDataRequiredException(InnerError.LOST_IDENTIFIER);

		Optional<Product> product = productRepo.findById(id);
		if(product.isEmpty())
			throw new RecordNotFoundException(InnerError.RECORD_NOT_FOUND, DomainName.Product.getMessage());

		productRepo.deleteById(id);
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
