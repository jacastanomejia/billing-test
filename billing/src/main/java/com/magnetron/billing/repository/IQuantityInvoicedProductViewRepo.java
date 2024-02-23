package com.magnetron.billing.repository;

import com.magnetron.billing.repository.entity.ProductWithQuantityInvoiced;
import com.magnetron.billing.repository.views.IReadOnlyRepository;

public interface IQuantityInvoicedProductViewRepo
        extends IReadOnlyRepository<ProductWithQuantityInvoiced, Long> {
}
