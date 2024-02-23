package com.magnetron.billing.service.contract;

import com.magnetron.billing.repository.entity.ProductWithGrossProfit;
import com.magnetron.billing.service.contract.base.IQueryViewService;

public interface IGrossProfitProductService
        extends IQueryViewService<ProductWithGrossProfit> {
}
