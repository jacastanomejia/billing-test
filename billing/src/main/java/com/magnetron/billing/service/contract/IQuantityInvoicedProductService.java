package com.magnetron.billing.service.contract;

import com.magnetron.billing.repository.entity.ProductWithQuantityInvoiced;
import com.magnetron.billing.service.contract.base.IQueryViewService;

public interface IQuantityInvoicedProductService
        extends IQueryViewService<ProductWithQuantityInvoiced> {
}
