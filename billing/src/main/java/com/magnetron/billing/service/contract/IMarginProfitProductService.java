package com.magnetron.billing.service.contract;

import com.magnetron.billing.repository.entity.ProductWithMarginProfit;
import com.magnetron.billing.service.contract.base.IQueryViewService;

public interface IMarginProfitProductService
        extends IQueryViewService<ProductWithMarginProfit> {
}
