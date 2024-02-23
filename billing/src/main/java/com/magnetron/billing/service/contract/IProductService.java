package com.magnetron.billing.service.contract;

import com.magnetron.billing.service.contract.base.ICrudService;
import com.magnetron.billing.service.contract.base.IQueryByIdService;
import com.magnetron.billing.service.dto.ProductDto;

public interface IProductService
        extends ICrudService<ProductDto, Long>,
                IQueryByIdService<ProductDto, Long> {
}
