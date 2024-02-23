package com.magnetron.billing.repository;

import com.magnetron.billing.repository.entity.PersonMoreExpensivePurchase;
import com.magnetron.billing.repository.views.IReadOnlyRepository;

public interface IMoreExpensivePurchaseViewRepo
        extends IReadOnlyRepository<PersonMoreExpensivePurchase, Long> {
}
