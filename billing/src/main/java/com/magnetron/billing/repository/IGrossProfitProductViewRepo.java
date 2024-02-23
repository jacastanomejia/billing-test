package com.magnetron.billing.repository;

import com.magnetron.billing.repository.entity.ProductWithGrossProfit;
import com.magnetron.billing.repository.views.IReadOnlyRepository;

public interface IGrossProfitProductViewRepo
        extends IReadOnlyRepository<ProductWithGrossProfit, Long> {
}
