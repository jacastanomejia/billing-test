package com.magnetron.billing.repository;

import com.magnetron.billing.repository.entity.ProductWithMarginProfit;
import com.magnetron.billing.repository.views.IReadOnlyRepository;

public interface IMarginProfitProductViewRepo
        extends IReadOnlyRepository<ProductWithMarginProfit, Long> {
}
