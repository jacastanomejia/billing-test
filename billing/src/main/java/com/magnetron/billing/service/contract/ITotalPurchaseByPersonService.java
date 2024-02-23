package com.magnetron.billing.service.contract;

import com.magnetron.billing.repository.entity.PersonWithTotalPurchase;
import com.magnetron.billing.service.contract.base.IQueryViewService;

public interface ITotalPurchaseByPersonService
        extends IQueryViewService<PersonWithTotalPurchase> {
}
