package com.magnetron.billing.repository;

import com.magnetron.billing.repository.entity.PersonWithTotalPurchase;
import com.magnetron.billing.repository.views.IReadOnlyRepository;

public interface ITotalPurchaseByPersonViewRepo
        extends IReadOnlyRepository<PersonWithTotalPurchase, Long> {

}